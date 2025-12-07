

package com.etc.pdsapp.dao;

import com.etc.pdsapp.db.DbConnect;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.Braid;
import com.etc.pdsapp.utils.StageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BraidDao {

    private static final String BASE_SELECT =
            """
            SELECT braid_id, stage_description, machine_id,
            user_id, deck_speed, speed, pitch , notes
            FROM pds.dbo.braid
            """;

    public ObservableList<Braid> getAllBraids() {
        ObservableList<Braid> list = FXCollections.observableArrayList();
        String sql = BASE_SELECT + "ORDER BY braid_id ASC";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "getAll", e, "sql", sql);
        }
        return list;
    }

    public Braid getBraidById(int id) {
        String sql = BASE_SELECT + "WHERE braid_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "getById", e, "sql", sql);
        }
        return null;
    }

    public int insertBraid(Braid b) {
        String sql = """
                INSERT INTO pds.dbo.braid
                (stage_description, machine_id, user_id, deck_speed, speed, pitch ,notes)
                VALUES (?, ?, ?, ?, ?, ? , ?)
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.getStageDescription());
            ps.setInt(2, b.getMachineId());
            if (b.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, b.getUserId());
            if (b.getDeckSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, b.getDeckSpeed());
            if (b.getSpeed() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, b.getSpeed());
            if (b.getPitch() == null) ps.setNull(6, Types.FLOAT); else ps.setDouble(6, b.getPitch());
            if (b.getNotes() == null) ps.setNull(7, Types.NVARCHAR); else ps.setString(7, b.getNotes());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "insert", e, "sql", sql);
        }
        return -1;
    }

    public boolean updateBraid(Braid b) {
        String sql = """
                UPDATE pds.dbo.braid SET
                stage_description = ?, machine_id = ?, user_id = ?,
                deck_speed = ?, speed = ?, pitch = ? ,notes = ?
                WHERE braid_id = ?
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, b.getStageDescription());
            ps.setInt(2, b.getMachineId());
            if (b.getUserId() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, b.getUserId());
            if (b.getDeckSpeed() == null) ps.setNull(4, Types.FLOAT); else ps.setDouble(4, b.getDeckSpeed());
            if (b.getSpeed() == null) ps.setNull(5, Types.FLOAT); else ps.setDouble(5, b.getSpeed());
            if (b.getPitch() == null) ps.setNull(6, Types.FLOAT); else ps.setDouble(6, b.getPitch());
            if (b.getNotes() == null) ps.setNull(7, Types.NVARCHAR); else ps.setString(7, b.getNotes());
            ps.setInt(8, b.getBraidId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "update", e, "sql", sql);
            return false;
        }
    }

    public boolean deleteBraid(int id) {
        String sql = """
        DELETE FROM pds.dbo.braid WHERE braid_id = ?
        """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "delete", e, "sql", sql);
            return false;
        }
    }



    public boolean existsBraidRecord(String stageDesc, int machineId) {
        String normalizedDesc = StageUtils.normalize(stageDesc);
        String sql = """
        SELECT stage_description
        FROM pds.dbo.braid
        WHERE machine_id = ?
    """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, machineId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String dbDesc = rs.getString("stage_description");
                    if (StageUtils.normalize(dbDesc).equals(normalizedDesc)) {
                        return true; // وجد تطابق
                    }
                }
            }
        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(), "existsBraidRecord", e, "sql", sql);
        }
        return false;
    }



    private static Braid mapRow(ResultSet rs) throws SQLException {
        return new Braid(
                rs.getInt("braid_id"),
                rs.getString("stage_description"),
                rs.getInt("machine_id"),
                (Integer) rs.getObject("user_id"),
                (Double) rs.getObject("deck_speed"),
                (Double) rs.getObject("speed"),
                (Double) rs.getObject("pitch"),
                rs.getString("notes")
        );
    }


    public Braid getByStageDescriptionAndMachine(String apiDescription, int machineId) {

        // Normalize API description
        String cleanApi = StageUtils.normalize(apiDescription);

        String sql = """
        SELECT TOP 1
            braid_id, stage_description, machine_id, user_id,
            deck_speed, speed, pitch, notes
        FROM pds.dbo.braid
        WHERE machine_id = ?
          AND LOWER(
                REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
                    stage_description,
                    ' ', ''),   -- إزالة المسافات
                    '-', ''),   -- إزالة الشرطات
                    '[', ''),   -- إزالة الأقواس
                    ']', ''),
                    '/', ''),
                    '.', '')    -- إزالة النقطة
          ) = ?
        ORDER BY braid_id DESC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, machineId);
            ps.setString(2, cleanApi);

//            System.out.println("=== NORMALIZED MATCH SEARCH ===");
//            System.out.println("Original API : " + apiDescription);
//            System.out.println("Clean API    : " + cleanApi);
//            System.out.println("Machine ID   : " + machineId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Braid braid = mapRow(rs);
                    //  System.out.println("MATCH FOUND: Speed=" + braid.getSpeed());
                    return braid;
                } else {
                    System.out.println("NO MATCH FOUND AFTER NORMALIZATION.");
                }
            }

        } catch (Exception e) {
            Logging.logException("ERROR", BraidDao.class.getName(),
                    "getByStageDescriptionAndMachine", e);
        }

        return null;
    }



    public Braid getLatestByStageDescriptionAndMachine(String stageDescription, int machineId) {
        String sql = BASE_SELECT +
                " WHERE stage_description = ? AND machine_id = ? " +
                " ORDER BY braid_id DESC"; // أحدث تسجيل أولاً

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, stageDescription.trim());
            ps.setInt(2, machineId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", BraidDao.class.getName(),
                    "getLatestByStageDescriptionAndMachine", e,
                    "stageDesc", stageDescription, "machineId", String.valueOf(machineId));
        }

        return null;
    }


}
