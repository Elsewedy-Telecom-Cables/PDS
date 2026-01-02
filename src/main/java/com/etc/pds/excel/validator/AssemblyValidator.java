package com.etc.pds.excel.validator;

import com.etc.pds.model.Assembly;
import com.etc.pds.logging.Logging;

import java.util.ArrayList;
import java.util.List;

public class AssemblyValidator {

    public List<String> validate(List<Assembly> assemblies) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < assemblies.size(); i++) {
            Assembly a = assemblies.get(i);
            String row = "Row " + (i + 1) + ": ";

            String action = a.getAction();
            int assemblyId = a.getAssemblyId();

            // 1. Action validation
            if (action == null || action.isBlank()) {
                errors.add(row + "Action is missing (must be INSERT, UPDATE or DELETE)");
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", row + "Missing action");
                continue; // Skip further checks if action is invalid
            }

            action = action.trim().toUpperCase();

            if (!action.equals("INSERT") && !action.equals("UPDATE") && !action.equals("DELETE")) {
                errors.add(row + "Invalid action: '" + a.getAction() + "' (allowed: INSERT, UPDATE, DELETE)");
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", row + "Invalid action: " + a.getAction());
            }

            // 2. ID required for UPDATE/DELETE
            if (("UPDATE".equals(action) || "DELETE".equals(action)) && assemblyId <= 0) {
                errors.add(row + "assembly_id is required and must be > 0 for action: " + action);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", row + "Missing or invalid ID for " + action);
            }

            // 3. Mandatory fields for all actions
            if (a.getStageDescription() == null || a.getStageDescription().trim().isEmpty()) {
                errors.add(row + "Stage Description is required");
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", row + "Empty Stage Description");
            }

            if (a.getMachineId() <= 0) {
                errors.add(row + "Machine ID is required and must be > 0");
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", row + "Invalid Machine ID: " + a.getMachineId());
            }

            // Note: All other fields (colors, lay lengths, line_speed, etc.) are optional → no validation needed
        }

        return errors;
    }
}
