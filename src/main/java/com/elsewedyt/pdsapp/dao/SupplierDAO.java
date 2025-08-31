package com.elsewedyt.pdsapp.dao;
import com.elsewedyt.pdsapp.db.DbConnect;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
public class SupplierDAO {
    // Get all suppliers
    public static ObservableList<Supplier> getAllSuppliers() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        String query = "SELECT supplier_id, supplier_name FROM calibration.dbo.suppliers ORDER BY supplier_id asc";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("supplier_id");
                String name = rs.getString("supplier_name");
                list.add(new Supplier(id, name));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDAO.class.getName(), "getAllSuppliers", e, "sql", query);
        }
        return list;
    }

    // Insert new supplier
    public static boolean insertSupplier(Supplier supplier) {
        String query = "INSERT INTO calibration.dbo.suppliers (supplier_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDAO.class.getName(), "insertSupplier", e, "sql", query);
        }
        return false;
    }

    // Update existing supplier
    public static boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE calibration.dbo.suppliers SET supplier_name = ? WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            ps.setInt(2, supplier.getSupplierId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDAO.class.getName(), "updateSupplier", e, "sql", query);
        }
        return false;
    }

    // Delete supplier
    public static boolean deleteSupplier(int supplierId) {
        String query = "DELETE FROM calibration.dbo.suppliers WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDAO.class.getName(), "deleteSupplier", e, "sql", query);
        }
        return false;
    }

    // Get supplier by ID
    public static Supplier getSupplierById(int supplierId) {
        String query = "SELECT supplier_id, supplier_name FROM calibration.dbo.suppliers WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("supplier_id");
                    String name = rs.getString("supplier_name");
                    return new Supplier(id, name);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDAO.class.getName(), "getSupplierById", e, "sql", query);
        }
        return null;
    }

}
