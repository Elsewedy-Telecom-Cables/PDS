package com.etc.pdsapp.excel.validator;

import com.etc.pdsapp.model.Assembly;
import com.etc.pdsapp.logging.Logging;

import java.util.ArrayList;
import java.util.List;

public class AssemblyValidator {

    public List<String> validate(List<Assembly> assemblies) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < assemblies.size(); i++) {
            Assembly a = assemblies.get(i);
            String rowIdentifier = "Row " + (i + 1) + ": ";

            //  Action Check
            if (a.getAction() == null ||
                    !(a.getAction().equalsIgnoreCase("INSERT") ||
                            a.getAction().equalsIgnoreCase("UPDATE") ||
                            a.getAction().equalsIgnoreCase("DELETE"))) {

                String msg = rowIdentifier + "Invalid action: " + a.getAction();
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            //   ID Check For  UPDATE/DELETE
            if ((a.getAction() != null &&
                    (a.getAction().equalsIgnoreCase("UPDATE") || a.getAction().equalsIgnoreCase("DELETE")))
                    && a.getAssemblyId() <= 0) {

                String msg = rowIdentifier + "ID is required for UPDATE/DELETE";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            // Check From Columns required
            if (a.getStageDescription() == null || a.getStageDescription().isEmpty()) {
                String msg = rowIdentifier + "Stage Description is required";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            if (a.getMachineId() <= 0) {
                String msg = rowIdentifier + "Machine ID is required and must be > 0";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            // Columns nullable like userId, lineSpeed, traverseLay, notes accept null
        }

        return errors;
    }
}
