package com.etc.pds.dao;

import com.etc.pds.db.DbConnect;
import com.etc.pds.logging.Logging;
import com.etc.pds.model.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StageDao {
    public ObservableList<Stage> getAllStages() {
        ObservableList<Stage> list = FXCollections.observableArrayList();
        String query = """
            SELECT stage_id, stage_name
            FROM pds.dbo.stages
            ORDER BY stage_name asc
            """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("stage_id");
                String name = rs.getString("stage_name");
                list.add(new Stage(id, name));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", StageDao.class.getName(), "getAllStages", e, "sql", query);
        }
        return list;
    }

}
