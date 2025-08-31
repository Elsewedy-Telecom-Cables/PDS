
package com.elsewedyt.pdsapp.dao;

import com.elsewedyt.pdsapp.db.DEF;
import com.elsewedyt.pdsapp.db.DbConnect;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.User;
import com.elsewedyt.pdsapp.models.UserContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
public class UserDAO {
    public static User checkConfirmPassword(String username, String pass) {
        User user = null;
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_USERNAME + " = ? AND " + DEF.USERS_PASSWORD + " = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt(DEF.USERS_ID));
                    user.setEmpCode(rs.getInt(DEF.USERS_EMP_ID));
                    user.setUserName(rs.getString(DEF.USERS_USERNAME));
                    user.setPassword(rs.getString(DEF.USERS_PASSWORD));
                    user.setFullName(rs.getString(DEF.USERS_FULLNAME));
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "checkConfirmPassword", e, "sql", query);
        }
        return user;
    }

    public static ObservableList<User> getUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_PHONE + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " ORDER BY " + DEF.USERS_ROLE + " DESC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt(DEF.USERS_ID),
                        rs.getInt(DEF.USERS_EMP_ID),
                        rs.getString(DEF.USERS_USERNAME),
                        rs.getString(DEF.USERS_PASSWORD),
                        rs.getString(DEF.USERS_FULLNAME),
                        rs.getString(DEF.USERS_PHONE),
                        rs.getInt(DEF.USERS_ROLE),
                        rs.getInt(DEF.USERS_ACTIVE),
                        rs.getString(DEF.USERS_CREATION_DATE)
                ));
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", UserDAO.class.getName(), "getUsers", ex);
        }
        return list;
    }

    public static ObservableList<User> getUsersByRoles(List<Integer> allowedRoles) {
        ObservableList<User> list = FXCollections.observableArrayList();
        if (allowedRoles == null || allowedRoles.isEmpty()) return list;

        String placeholders = allowedRoles.stream()
                .map(r -> "?")
                .collect(Collectors.joining(","));
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_PHONE + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_ROLE + " IN (" + placeholders + ")";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < allowedRoles.size(); i++) {
                ps.setInt(i + 1, allowedRoles.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(
                            rs.getInt(DEF.USERS_ID),
                            rs.getInt(DEF.USERS_EMP_ID),
                            rs.getString(DEF.USERS_USERNAME),
                            rs.getString(DEF.USERS_PASSWORD),
                            rs.getString(DEF.USERS_FULLNAME),
                            rs.getString(DEF.USERS_PHONE),
                            rs.getInt(DEF.USERS_ROLE),
                            rs.getInt(DEF.USERS_ACTIVE),
                            rs.getString(DEF.USERS_CREATION_DATE)
                    ));
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", UserDAO.class.getName(), "getUsersByRoles", ex);
        }
        return list;
    }

    public static ObservableList<String> getAllUsersFullName() {
        ObservableList<String> userFullNames = FXCollections.observableArrayList();
        String query = "SELECT " + DEF.USERS_FULLNAME +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " ORDER BY " + DEF.USERS_FULLNAME + " ASC";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userFullNames.add(rs.getString(DEF.USERS_FULLNAME));
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getAllUsersFullName", e, "sql", query);
        }
        return userFullNames;
    }

    public static User loadUserData(int userId) {
        User user = null;
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_PHONE + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_ID + " = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt(DEF.USERS_ID),
                            rs.getInt(DEF.USERS_EMP_ID),
                            rs.getString(DEF.USERS_USERNAME),
                            rs.getString(DEF.USERS_PASSWORD),
                            rs.getString(DEF.USERS_FULLNAME),
                            rs.getString(DEF.USERS_PHONE),
                            rs.getInt(DEF.USERS_ROLE),
                            rs.getInt(DEF.USERS_ACTIVE),
                            rs.getString(DEF.USERS_CREATION_DATE)
                    );
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "loadUserData", e, "sql", query);
        }
        return user;
    }

    public static boolean insertUser(User us) {
        String query = "INSERT INTO " + DEF.DB_NAME + "." + DEF.USERS_TABLE + " (" +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_PHONE + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE + ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "insertUser", e, "sql", query);
        }
        return false;
    }

    public static User getUserByEmpId(int emp_id) {
        User us = new User();
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_PHONE + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_EMP_ID + " = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, emp_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    us.setUserId(rs.getInt(DEF.USERS_ID));
                    us.setEmpCode(rs.getInt(DEF.USERS_EMP_ID));
                    us.setUserName(rs.getString(DEF.USERS_USERNAME));
                    us.setPassword(rs.getString(DEF.USERS_PASSWORD));
                    us.setFullName(rs.getString(DEF.USERS_FULLNAME));
                    us.setPhone(rs.getString(DEF.USERS_PHONE));
                    us.setRole(rs.getInt(DEF.USERS_ROLE));
                    us.setActive(rs.getInt(DEF.USERS_ACTIVE));
                    us.setCreationDate(rs.getString(DEF.USERS_CREATION_DATE));
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getUserByEmpId", e, "sql", query);
        }
        return us;
    }

    public static User getUserByUsername(String username) {
        User user = null;
        String query = "SELECT " +
                DEF.USERS_ID + ", " +
                DEF.USERS_EMP_ID + ", " +
                DEF.USERS_USERNAME + ", " +
                DEF.USERS_PASSWORD + ", " +
                DEF.USERS_FULLNAME + ", " +
                DEF.USERS_ROLE + ", " +
                DEF.USERS_ACTIVE + ", " +
                DEF.USERS_CREATION_DATE +
                " FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_USERNAME + " = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt(DEF.USERS_ID));
                    user.setEmpCode(rs.getInt(DEF.USERS_EMP_ID));
                    user.setUserName(rs.getString(DEF.USERS_USERNAME));
                    user.setPassword(rs.getString(DEF.USERS_PASSWORD));
                    user.setFullName(rs.getString(DEF.USERS_FULLNAME));
                    user.setRole(rs.getInt(DEF.USERS_ROLE));
                    user.setActive(rs.getInt(DEF.USERS_ACTIVE));
                    user.setCreationDate(rs.getString(DEF.USERS_CREATION_DATE));
                    UserContext.setCurrentUser(user);
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getUserByUsername", e, "sql", query);
        }
        return user;
    }

    public static boolean updateUser(User us) {
        String query = "UPDATE " + DEF.DB_NAME + "." + DEF.USERS_TABLE + " SET " +
                DEF.USERS_PASSWORD + " = ?, " +
                DEF.USERS_FULLNAME + " = ?, " +
                DEF.USERS_PHONE + " = ?, " +
                DEF.USERS_ROLE + " = ?, " +
                DEF.USERS_ACTIVE + " = ? " +
                "WHERE " + DEF.USERS_EMP_ID + " = ?";
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
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "updateUser", e, "sql", query);
        }
        return false;
    }

    public static boolean deleteUser(int emp_id) {
        String query = "DELETE FROM " + DEF.DB_NAME + "." + DEF.USERS_TABLE +
                " WHERE " + DEF.USERS_EMP_ID + " = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, emp_id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "deleteUser", e, "sql", query);
        }
        return false;
    }
}
