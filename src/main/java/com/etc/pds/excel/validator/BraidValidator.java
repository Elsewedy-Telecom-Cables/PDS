package com.etc.pds.excel.validator;

import com.etc.pds.model.Braid;
import com.etc.pds.logging.Logging;

import java.util.ArrayList;
import java.util.List;

public class BraidValidator {

    public List<String> validate(List<Braid> braids) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < braids.size(); i++) {
            Braid b = braids.get(i);
            String rowIdentifier = "Row " + (i + 1) + ": ";

            // Action Check
            if (b.getAction() == null ||
                    !(b.getAction().equalsIgnoreCase("INSERT") ||
                            b.getAction().equalsIgnoreCase("UPDATE") ||
                            b.getAction().equalsIgnoreCase("DELETE"))) {

                String msg = rowIdentifier + "Invalid action: " + b.getAction();
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            // ID Check For UPDATE/DELETE
            if ((b.getAction() != null &&
                    (b.getAction().equalsIgnoreCase("UPDATE") || b.getAction().equalsIgnoreCase("DELETE")))
                    && b.getBraidId() <= 0) {

                String msg = rowIdentifier + "ID is required for UPDATE/DELETE";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            // Check From Columns required
            if (b.getStageDescription() == null || b.getStageDescription().isEmpty()) {
                String msg = rowIdentifier + "Stage Description is required";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            if (b.getMachineId() <= 0) {
                String msg = rowIdentifier + "Machine ID is required and must be > 0";
                errors.add(msg);
                Logging.logMessage(Logging.WARN, getClass().getName(), "validate", msg);
            }

            // Columns nullable like userId, deck_speed, speed, pitch, notes accept null
        }

        return errors;
    }
}