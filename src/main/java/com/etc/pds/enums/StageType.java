package com.etc.pds.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum StageType {
    INSULATION(1, "Insulation"),
    ASSEMBLY(2, "Assembly"),
    SHEATHING_DATA(3, "Sheathing_Data"),
    BRAID(4, "Braid"),
    BUFFERING(5, "Buffering"),
    STRANDING(6, "Stranding"),
    COILING(7, "Coiling"),
    REWINDING(8, "Rewinding"),
    FO_SHEATHING(9, "Fo_Sheathing"),
    PREMISES(10, "Premises");

    private final int id;
    private final String tableName;
    private final String displayName;

    private static final Map<Integer, StageType> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(StageType::getId, e -> e));

    StageType(int id, String tableName) {
        this(id, tableName, formatDisplayName(tableName));
    }

    StageType(int id, String tableName, String displayName) {
        this.id = id;
        this.tableName = tableName;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StageType fromId(int id) {
        return BY_ID.get(id);
    }

    public static StageType fromTableName(String tableName) {
        if (tableName == null) return null;
        return Arrays.stream(values())
                .filter(s -> s.tableName.equalsIgnoreCase(tableName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return displayName;
    }

    private static String formatDisplayName(String tableName) {
        return tableName.replace("_", " ")
                .replace("Fo ", "FO ")
                .trim();
    }

}
