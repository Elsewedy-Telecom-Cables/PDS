//package com.etc.pds.dao;
//import com.etc.pds.db.DbConnect;
//import com.etc.pds.logging.Logging;
//import com.etc.pds.model.Assembly;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import java.sql.*;
//
//public class AssemblyDao {
//
//    private static final String BASE_SELECT =
//           """
//           SELECT assembly_id, stage_description,
//                  machine_id, user_id, line_speed,
//                  traverse_lay , notes
//                   FROM pds.dbo.assembly
//           """;
//
//    public  ObservableList<Assembly> getAll() {
//        ObservableList<Assembly> list = FXCollections.observableArrayList();
//        String sql = BASE_SELECT + "ORDER BY assembly_id ASC";
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) list.add(mapRow(rs));
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "getAll", e, "sql", sql);
//        }
//        return list;
//    }
//
//    public  Assembly getById(int id) {
//        String sql = BASE_SELECT + "WHERE assembly_id = ?";
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) return mapRow(rs);
//            }
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "getById", e, "sql", sql);
//        }
//        return null;
//    }
//
//    // returns generated ID (recommended)
//    public  int insert(Assembly a) {
//        // Check Before Insert
//        if (existsAssemblyRecord(a.getStageDescription(), a.getMachineId())) {
//            Logging.logMessage(Logging.WARN, AssemblyDao.class.getName(), "insert",
//                    "Duplicate assembly found: stage=%s, machineId=%d", a.getStageDescription(), a.getMachineId());
//            return -1; //
//        }
//        String sql = """
//                INSERT INTO pds.dbo.assembly
//                (stage_description, machine_id, user_id, line_speed, traverse_lay ,notes)
//                VALUES (?, ?, ?, ?, ?, ?)
//                """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            ps.setString(1, a.getStageDescription());
//            ps.setInt(2, a.getMachineId());
//            if (a.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getUserId());
//            if (a.getLineSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, a.getLineSpeed());
//            if (a.getTraverseLay() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, a.getTraverseLay());
//            if (a.getNotes() == null) ps.setNull(6, Types.NVARCHAR); else ps.setString(6, a.getNotes());
//
//            int affected = ps.executeUpdate();
//            if (affected > 0) {
//                try (ResultSet keys = ps.getGeneratedKeys()) {
//                    if (keys.next()) return keys.getInt(1);
//                }
//            }
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "insert", e, "sql", sql);
//        }
//        return -1;
//    }
//
//    public  boolean update(Assembly a) {
//        String sql = """
//                UPDATE pds.dbo.assembly SET
//                stage_description = ?, machine_id = ?, user_id = ?,
//                line_speed = ?, traverse_lay = ? ,notes = ?
//                WHERE assembly_id = ?
//                """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, a.getStageDescription());
//            ps.setInt(2, a.getMachineId());
//            if (a.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getUserId());
//            if (a.getLineSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, a.getLineSpeed());
//            if (a.getTraverseLay() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, a.getTraverseLay());
//            if (a.getNotes() == null) ps.setNull(6, Types.NVARCHAR); else ps.setString(6, a.getNotes());
//            ps.setInt(7, a.getAssemblyId());
//
//            return ps.executeUpdate() > 0;
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "update", e, "sql", sql);
//            return false;
//        }
//    }
//
//    public  boolean delete(int id) {
//        String sql = """
//        DELETE FROM pds.dbo.assembly WHERE assembly_id = ?
//        """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeUpdate() > 0;
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "delete", e, "sql", sql);
//            return false;
//        }
//    }
//
//    public  boolean existsAssemblyRecord(String stageDescription, int machineId) {
//        String query = """
//        SELECT COUNT(*) FROM pds.dbo.assembly
//        WHERE stage_description = ? AND machine_id = ?
//        """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//            ps.setString(1, stageDescription);
//            ps.setInt(2, machineId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", "AssemblyDao", "existsAssemblyRecord", e, "sql", query);
//        }
//        return false;
//    }
//
//    private  Assembly mapRow(ResultSet rs) throws SQLException {
//        return new Assembly(
//                rs.getInt("assembly_id"),
//                rs.getString("stage_description"),
//                rs.getInt("machine_id"),
//                (Integer) rs.getObject("user_id"),
//                (Double) rs.getObject("line_speed"),
//                (Double) rs.getObject("traverse_lay"),
//                rs.getString("notes")
//        );
//    }
//
//}

package com.etc.pds.dao;

import com.etc.pds.db.DbConnect;
import com.etc.pds.logging.Logging;
import com.etc.pds.model.Assembly;
import com.etc.pds.utils.StageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.*;

public class AssemblyDao {

    private static final String BASE_SELECT = """
        SELECT assembly_id, stage_description, machine_id, user_id,
               line_speed, traverse_lay, lay_length,
               pair_1_lay_length, pair_2_lay_length, pair_3_lay_length, pair_4_lay_length,
               pair_1_color, pair_2_color, pair_3_color, pair_4_color,
               notes
        FROM pds.dbo.assembly 
        """;

    public ObservableList<Assembly> getAll() {
        ObservableList<Assembly> list = FXCollections.observableArrayList();
        String sql = BASE_SELECT + "ORDER BY assembly_id ASC";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "getAll", e, "sql", sql);
        }
        return list;
    }

    public Assembly getById(int id) {
        String sql = BASE_SELECT + "WHERE assembly_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "getById", e, "sql", sql);
        }
        return null;
    }

    public int insert(Assembly a) {
        String normalized = StageUtils.normalize(a.getStageDescription());
        if (existsAssemblyRecord(normalized, a.getMachineId())) {
            Logging.logMessage(Logging.WARN, AssemblyDao.class.getName(), "insert",
                    "Duplicate assembly skipped: stage=%s, machineId=%d", a.getStageDescription(), a.getMachineId());
            return -1;
        }

        String sql = """
            INSERT INTO pds.dbo.assembly (
                stage_description, machine_id, user_id, line_speed, traverse_lay, lay_length,
                pair_1_lay_length, pair_2_lay_length, pair_3_lay_length, pair_4_lay_length,
                pair_1_color, pair_2_color, pair_3_color, pair_4_color, notes
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getStageDescription());
            ps.setInt(2, a.getMachineId());
            setNullableInt(ps, 3, a.getUserId());
            setNullableDouble(ps, 4, a.getLineSpeed());
            setNullableDouble(ps, 5, a.getTraverseLay());
            setNullableBigDecimal(ps, 6, a.getLayLength());
            setNullableBigDecimal(ps, 7, a.getPair1LayLength());
            setNullableBigDecimal(ps, 8, a.getPair2LayLength());
            setNullableBigDecimal(ps, 9, a.getPair3LayLength());
            setNullableBigDecimal(ps, 10, a.getPair4LayLength());
            ps.setString(11, a.getPair1Color());
            ps.setString(12, a.getPair2Color());
            ps.setString(13, a.getPair3Color());
            ps.setString(14, a.getPair4Color());
            ps.setString(15, a.getNotes());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "insert", e, "sql", sql);
        }
        return -1;
    }

    public boolean update(Assembly a) {
        String sql = """
            UPDATE pds.dbo.assembly SET 
                stage_description = ?, machine_id = ?, user_id = ?, line_speed = ?, traverse_lay = ?, lay_length = ?,
                pair_1_lay_length = ?, pair_2_lay_length = ?, pair_3_lay_length = ?, pair_4_lay_length = ?,
                pair_1_color = ?, pair_2_color = ?, pair_3_color = ?, pair_4_color = ?, notes = ?
            WHERE assembly_id = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getStageDescription());
            ps.setInt(2, a.getMachineId());
            setNullableInt(ps, 3, a.getUserId());
            setNullableDouble(ps, 4, a.getLineSpeed());
            setNullableDouble(ps, 5, a.getTraverseLay());
            setNullableBigDecimal(ps, 6, a.getLayLength());
            setNullableBigDecimal(ps, 7, a.getPair1LayLength());
            setNullableBigDecimal(ps, 8, a.getPair2LayLength());
            setNullableBigDecimal(ps, 9, a.getPair3LayLength());
            setNullableBigDecimal(ps, 10, a.getPair4LayLength());
            ps.setString(11, a.getPair1Color());
            ps.setString(12, a.getPair2Color());
            ps.setString(13, a.getPair3Color());
            ps.setString(14, a.getPair4Color());
            ps.setString(15, a.getNotes());
            ps.setInt(16, a.getAssemblyId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "update", e, "sql", sql);
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM pds.dbo.assembly WHERE assembly_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "delete", e, "sql", sql);
            return false;
        }
    }

    // Critical: Same logic as BraidDao.existsBraidRecord but with normalization
    public boolean existsAssemblyRecord(String stageDescription, int machineId) {
        String normalized = StageUtils.normalize(stageDescription);
        String sql = "SELECT stage_description FROM pds.dbo.assembly WHERE machine_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, machineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String dbDesc = rs.getString("stage_description");
                    if (dbDesc != null && StageUtils.normalize(dbDesc).equals(normalized)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", AssemblyDao.class.getName(), "existsAssemblyRecord", e, "sql", sql);
        }
        return false;
    }

    // Critical function: Used by API matching
    public Assembly getByStageDescriptionAndMachine(String apiDescription, int machineId) {
        String cleanApi = StageUtils.normalize(apiDescription);
        String sql = """
            SELECT TOP 1 assembly_id, stage_description, machine_id, user_id, 
                   line_speed, traverse_lay, lay_length,
                   pair_1_lay_length, pair_2_lay_length, pair_3_lay_length, pair_4_lay_length,
                   pair_1_color, pair_2_color, pair_3_color, pair_4_color, notes
            FROM pds.dbo.assembly 
            WHERE machine_id = ? 
              AND LOWER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
                  stage_description, ' ', ''), '-', ''), '[', ''), ']', ''), '/', ''), '.', '')) = ?
            ORDER BY assembly_id DESC
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, machineId);
            ps.setString(2, cleanApi);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            Logging.logException("ERROR", AssemblyDao.class.getName(), "getByStageDescriptionAndMachine", e);
        }
        return null;
    }

    private Assembly mapRow(ResultSet rs) throws SQLException {
        return new Assembly(
                rs.getInt("assembly_id"),
                rs.getString("stage_description"),
                rs.getInt("machine_id"),
                (Integer) rs.getObject("user_id"),
                (Double) rs.getObject("line_speed"),
                (Double) rs.getObject("traverse_lay"),
                (BigDecimal) rs.getObject("lay_length"),
                (BigDecimal) rs.getObject("pair_1_lay_length"),
                (BigDecimal) rs.getObject("pair_2_lay_length"),
                (BigDecimal) rs.getObject("pair_3_lay_length"),
                (BigDecimal) rs.getObject("pair_4_lay_length"),
                rs.getString("pair_1_color"),
                rs.getString("pair_2_color"),
                rs.getString("pair_3_color"),
                rs.getString("pair_4_color"),
                rs.getString("notes")
        );
    }

    // Helper methods to reduce repetition
    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) ps.setNull(index, Types.INTEGER);
        else ps.setInt(index, value);
    }

    private void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) ps.setNull(index, Types.FLOAT);
        else ps.setDouble(index, value);
    }

    private void setNullableBigDecimal(PreparedStatement ps, int index, BigDecimal value) throws SQLException {
        if (value == null) ps.setNull(index, Types.DECIMAL);
        else ps.setBigDecimal(index, value);
    }
}
