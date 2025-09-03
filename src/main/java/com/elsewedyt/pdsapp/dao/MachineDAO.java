package com.elsewedyt.pdsapp.dao;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Machine;
import com.elsewedyt.pdsapp.db.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MachineDAO {
        // Get all machines
        public static ObservableList<Machine> getAllMachines() {
            ObservableList<Machine> list = FXCollections.observableArrayList();
            String query = "SELECT machine_id, machine_name, stage_id FROM pds.dbo.machines ORDER BY machine_id asc ";
            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("machine_id");
                    String name = rs.getString("machine_name");
                    int stageId =  rs.getInt("stage_id");
                    list.add(new Machine(id, name,stageId));
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "getAllMachines", e, "sql", query);
            }
            return list;
        }
            // Insert new machine
            public static boolean insertMachine(Machine machine) {
                String query = "INSERT INTO pds.dbo.machines (machine_name,stage_id) VALUES (?,?)";
                try (Connection con = DbConnect.getConnect();
                     PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setString(1, machine.getMachineName());
                    if (machine.getStageId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, machine.getStageId());
                    // Ai If i need write with ternary operator this is correct ?
                 //   Integer stage = machine.getStageId() != null ? ps.setObject(2, Integer) : "";

                    return ps.executeUpdate() > 0;

                } catch (Exception e) {
                    Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "insertMachine", e, "sql", query);
                }
                return false;
            }

            // Update existing machine
            public static boolean updateMachine(Machine machine) {
                String query = "UPDATE pds.dbo.machines SET machine_name = ? , stage_id = ? WHERE machine_id = ?";
                try (Connection con = DbConnect.getConnect();
                     PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setString(1, machine.getMachineName());
                    if (machine.getStageId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, machine.getStageId());
                    ps.setInt(3, machine.getMachineId());
                    return ps.executeUpdate() > 0;

                } catch (Exception e) {
                    Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "updateMachine", e, "sql", query);
                }
                return false;
            }

            // Delete machine by ID
            public static boolean deleteMachine(int machineId) {
                String query = "DELETE FROM pds.dbo.machines WHERE machine_id = ?";
                try (Connection con = DbConnect.getConnect();
                     PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setInt(1, machineId);
                    return ps.executeUpdate() > 0;

                } catch (Exception e) {
                    Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "deleteMachine", e, "sql", query);
                }
                return false;
            }



            // Get machine by ID
            public static Machine getMachineById(int machineId) {
                String query = "SELECT machine_id, machine_name FROM pds.dbo.machines WHERE machine_id = ?";
                try (Connection con = DbConnect.getConnect();
                     PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setInt(1, machineId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return new Machine(rs.getInt("machine_id"), rs.getString("machine_name"),rs.getInt("stage_id"));
                    }

                } catch (Exception e) {
                    Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "getMachineById", e, "sql", query);
                }
                return null;
            }
       // Get machine by StageId
        public static ObservableList<Machine> getMachinesByStageId(int stageId) {
            ObservableList<Machine> list = FXCollections.observableArrayList();
        String query = "SELECT machine_id, machine_name , stage_id FROM pds.dbo.machines WHERE stage_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, stageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                  list.add(new Machine(rs.getInt("machine_id"),
                        rs.getString("machine_name"),
                        rs.getInt("stage_id")));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "getMachineByStageId", e, "sql", query);
        }
        return list;
    }

             public static int getMachinesCount() {
            String query = "SELECT COUNT(*) AS total FROM pds.dbo.machines";
            int count = 0;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    count = rs.getInt("total");
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "getMachinesCount", e, "sql", query);
            }

            return count;
        }

            public static ObservableList<String> getAllMachineNames() {
            ObservableList<String> machineNames = FXCollections.observableArrayList();
            String query = "SELECT  machine_name FROM pds.dbo.machines ORDER BY machine_id asc";
            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    machineNames.add(rs.getString("machine_name"));
                }
            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", MachineDAO.class.getName(), "getAllMachineNames", e, "sql", query);
            }
            return machineNames;
        }





}
