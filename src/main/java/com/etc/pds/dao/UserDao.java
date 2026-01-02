
package com.etc.pds.dao;

import com.etc.pds.db.DbConnect;
import com.etc.pds.logging.Logging;
import com.etc.pds.model.User;
import com.etc.pds.model.UserContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
public class UserDao {

    public  User checkConfirmPassword(String username, String pass) {
        User user = null;

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name
            FROM pds.dbo.users
            WHERE user_name = ? AND password = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmpCode(rs.getInt("emp_code"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "checkConfirmPassword", e, "sql", query);
        }

        return user;
    }

    public  ObservableList<User> getUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name,
                   phone, role, active, creation_date
            FROM pds.dbo.users
            ORDER BY role DESC
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getInt("emp_code"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getInt("role"),
                        rs.getInt("active"),
                        rs.getString("creation_date")
                ));
            }

        } catch (Exception e) {
            Logging.logException("ERROR", UserDao.class.getName(), "getUsers", e);
        }

        return list;
    }

    public  ObservableList<User> getUsersByRoles(List<Integer> allowedRoles) {
        ObservableList<User> list = FXCollections.observableArrayList();
        if (allowedRoles == null || allowedRoles.isEmpty()) return list;

        String placeholders = allowedRoles.stream()
                .map(r -> "?")
                .collect(Collectors.joining(","));

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name,
                   phone, role, active, creation_date
            FROM pds.dbo.users
            WHERE role IN (%s)
            """.formatted(placeholders);

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            for (int i = 0; i < allowedRoles.size(); i++) {
                ps.setInt(i + 1, allowedRoles.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date")
                    ));
                }
            }

        } catch (Exception e) {
            Logging.logException("ERROR", UserDao.class.getName(), "getUsersByRoles", e);
        }

        return list;
    }

    public  ObservableList<String> getAllUsersFullName() {
        ObservableList<String> list = FXCollections.observableArrayList();

        String query = """
            SELECT full_name
            FROM pds.dbo.users
            ORDER BY full_name ASC
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("full_name"));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "getAllUsersFullName", e, "sql", query);
        }

        return list;
    }

    public  User loadUserData(int userId) {
        User user = null;

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name,
                   phone, role, active, creation_date
            FROM pds.dbo.users
            WHERE user_id = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "loadUserData", e, "sql", query);
        }

        return user;
    }

    public  boolean  insertUser(User us) {
        String query = """
            INSERT INTO pds.dbo.users
            (emp_code, user_name, password, full_name, phone, role, active, creation_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, us.getEmpCode());
            ps.setString(2, us.getUserName());
            ps.setString(3, us.getPassword());
            ps.setString(4, us.getFullName());
            ps.setString(5, us.getPhone());
            ps.setInt(6, us.getRole());
            ps.setInt(7, us.getActive());
            ps.setString(8, us.getCreationDate());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "insertUser", e, "sql", query);
        }

        return false;
    }

    public  User getUserByEmpId(int empId) {
        User user = null;

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name,
                   phone, role, active, creation_date
            FROM pds.dbo.users
            WHERE emp_code = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "getUserByEmpId", e, "sql", query);
        }

        return user;
    }


    public  User getUserByUsername(String username) {
        User user = null;

        String query = """
            SELECT user_id, emp_code, user_name, password, full_name,
                   role, active, creation_date
            FROM pds.dbo.users
            WHERE user_name = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmpCode(rs.getInt("emp_code"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getInt("role"));
                    user.setActive(rs.getInt("active"));
                    user.setCreationDate(rs.getString("creation_date"));
                    UserContext.setCurrentUser(user);

                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "getUserByUsername", e, "sql", query);
        }

        return user;
    }


    public  boolean updateUser(User us) {
        String query = """
            UPDATE pds.dbo.users
            SET password = ?,
                full_name = ?,
                phone = ?,
                role = ?,
                active = ?
            WHERE emp_code = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, us.getPassword());
            ps.setString(2, us.getFullName());
            ps.setString(3, us.getPhone());
            ps.setInt(4, us.getRole());
            ps.setInt(5, us.getActive());
            ps.setInt(6, us.getEmpCode());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "updateUser", e, "sql", query);
        }

        return false;
    }

    public  boolean deleteUser(int empId) {
        String query = """
            DELETE FROM pds.dbo.users
            WHERE emp_code = ?
            """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDao.class.getName(), "deleteUser", e, "sql", query);
        }

        return false;
    }

}
