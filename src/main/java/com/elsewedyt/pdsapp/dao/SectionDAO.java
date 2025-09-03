package com.elsewedyt.pdsapp.dao;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Section;
import com.elsewedyt.pdsapp.db.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class SectionDAO {

        // Get all sections
        public static ObservableList<Section> getAllSections() {
            ObservableList<Section> list = FXCollections.observableArrayList();
            String query = "SELECT section_id, section_name FROM pds.dbo.sections ORDER BY section_id asc ";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("section_id");
                    String name = rs.getString("section_name");
                    list.add(new Section(id, name));
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "getAllSections", e, "sql", query);
            }
            return list;
        }

        // Get a single section by ID
        public static Section getSectionById(int sectionId) {
            String query = "SELECT section_id, section_name FROM pds.dbo.sections WHERE section_id = ?";
            Section section = null;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, sectionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("section_id");
                        String name = rs.getString("section_name");
                        section = new Section(id, name);
                    }
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "getSectionById", e, "sql", query);
            }
            return section;
        }

        // Insert a new section
        public static int insertSection(Section section) {
            String query = "INSERT INTO pds.dbo.sections (section_name) VALUES (?)";
            int generatedId = -1;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, section.getSectionName());
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            generatedId = rs.getInt(1);
                        }
                    }
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "insertSection", e, "sql", query);
            }
            return generatedId;
        }

    // Insert a new section
//    public static boolean insertSectionBoolean(Section section) {
//        String query = "INSERT INTO pds.dbo.sections (section_name) VALUES (?)";
//        // int generatedId = -1;
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            ps.setString(1, section.getSectionName());
//            return   ps.executeUpdate();
//
//                        return ps.executeUpdate() > 1 ;
//                        //  generatedId = rs.getInt(1);
//
//            }
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "insertSection", e, "sql", query);
//        }
//        return false;
//    }

        // Update an existing section
        public static boolean updateSection(Section section) {
            String query = "UPDATE pds.dbo.sections SET section_name = ? WHERE section_id = ?";
            boolean success = false;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, section.getSectionName());
                ps.setInt(2, section.getSectionId());
                int affectedRows = ps.executeUpdate();
                success = affectedRows > 0;

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "updateSection", e, "sql", query);
            }
            return success;
        }

        // Delete a section
        public static boolean deleteSection(int sectionId) {
            String query = "DELETE FROM pds.dbo.sections WHERE section_id = ?";
            boolean success = false;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, sectionId);
                int affectedRows = ps.executeUpdate();
                success = affectedRows > 0;

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "deleteSection", e, "sql", query);
            }
            return success;
        }


    }


