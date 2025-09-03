package com.elsewedyt.pdsapp.dao;
import com.elsewedyt.pdsapp.db.DbConnect;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Assembly;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class AssemblyDAO {
    private static final String BASE_SELECT =
            "SELECT assembly_id, stage_description, machine_id, user_id, line_speed, traverse_lay , notes " +
                    "FROM pds.dbo.assembly ";

    public static ObservableList<Assembly> getAll() {
        ObservableList<Assembly> list = FXCollections.observableArrayList();
        String sql = BASE_SELECT + "ORDER BY assembly_id ASC";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDAO.class.getName(), "getAll", e, "sql", sql);
        }
        return list;
    }

    public static Assembly getById(int id) {
        String sql = BASE_SELECT + "WHERE assembly_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDAO.class.getName(), "getById", e, "sql", sql);
        }
        return null;
    }

    // returns generated ID (recommended)
    public static int insert(Assembly a) {
        // Check Before Insert
        if (existsAssemblyRecord(a.getStageDescription(), a.getMachineId())) {
            Logging.logMessage(Logging.WARN, AssemblyDAO.class.getName(), "insert",
                    "Duplicate assembly found: stage=%s, machineId=%d", a.getStageDescription(), a.getMachineId());
            return -1; //
        }
        String sql = "INSERT INTO pds.dbo.assembly " +
                "(stage_description, machine_id, user_id, line_speed, traverse_lay ,notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getStageDescription());
            ps.setInt(2, a.getMachineId());
            if (a.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getUserId());
            if (a.getLineSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, a.getLineSpeed());
            if (a.getTraverseLay() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, a.getTraverseLay());
            if (a.getNotes() == null) ps.setNull(6, Types.NVARCHAR); else ps.setString(6, a.getNotes());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDAO.class.getName(), "insert", e, "sql", sql);
        }
        return -1;
    }

    public static boolean update(Assembly a) {
        String sql = "UPDATE pds.dbo.assembly SET " +
                "stage_description = ?, machine_id = ?, user_id = ?, line_speed = ?, traverse_lay = ? ,notes = ? " +
                "WHERE assembly_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getStageDescription());
            ps.setInt(2, a.getMachineId());
            if (a.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getUserId());
            if (a.getLineSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, a.getLineSpeed());
            if (a.getTraverseLay() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, a.getTraverseLay());
            if (a.getNotes() == null) ps.setNull(6, Types.NVARCHAR); else ps.setString(6, a.getNotes());
            ps.setInt(7, a.getAssemblyId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDAO.class.getName(), "update", e, "sql", sql);
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM pds.dbo.assembly WHERE assembly_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDAO.class.getName(), "delete", e, "sql", sql);
            return false;
        }
    }
    public static boolean existsAssemblyRecord(String stageDescription, int machineId) {
        String query = "SELECT COUNT(*) FROM pds.dbo.assembly WHERE stage_description = ? AND machine_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, stageDescription);
            ps.setInt(2, machineId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", "AssemblyDAO", "existsAssemblyRecord", e, "sql", query);
        }
        return false;
    }

    private static Assembly mapRow(ResultSet rs) throws SQLException {
        return new Assembly(
                rs.getInt("assembly_id"),
                rs.getString("stage_description"),
                rs.getInt("machine_id"),
                (Integer) rs.getObject("user_id"),
                (Double) rs.getObject("line_speed"),
                (Double) rs.getObject("traverse_lay"),
                rs.getString("notes")
        );
    }
}
