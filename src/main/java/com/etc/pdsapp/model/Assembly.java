//package com.etc.pdsapp.model;
//
//public class Assembly {
//    private int assemblyId;                 // IDENTITY
//    private String stageDescription;        // NOT NULL
//    private int machineId;                  // NOT NULL (FK)
//    private Integer userId;                 // NULLABLE (FK)
//    private Double lineSpeed;               // NULLABLE (float)
//    private Double traverseLay;             // NULLABLE (float)
//    private String notes ;
//    private String action ;
//
//    public Assembly() {}
//
//    public Assembly(int assemblyId, String stageDescription, int machineId, Integer userId,
//                    Double lineSpeed, Double traverseLay,String notes) {
//        this.assemblyId = assemblyId;
//        this.stageDescription = stageDescription;
//        this.machineId = machineId;
//        this.userId = userId;
//        this.lineSpeed = lineSpeed;
//        this.traverseLay = traverseLay;
//        this.notes = notes ;
//    }
//
//    // For Creation Without ID For Import  Excel
//    public Assembly(String stageDescription, int machineId, Integer userId,
//                    Double lineSpeed, Double traverseLay,String notes) {
//        this(0, stageDescription, machineId, userId, lineSpeed, traverseLay,notes);
//    }
//
//    public int getAssemblyId() { return assemblyId; }
//    public void setAssemblyId(int assemblyId) { this.assemblyId = assemblyId; }
//
//    public String getStageDescription() { return stageDescription; }
//    public void setStageDescription(String stageDescription) { this.stageDescription = stageDescription; }
//
//    public int getMachineId() { return machineId; }
//    public void setMachineId(int machineId) { this.machineId = machineId; }
//
//    public Integer getUserId() { return userId; }
//    public void setUserId(Integer userId) { this.userId = userId; }
//
//    public Double getLineSpeed() { return lineSpeed; }
//    public void setLineSpeed(Double lineSpeed) { this.lineSpeed = lineSpeed; }
//
//    public Double getTraverseLay() { return traverseLay; }
//    public void setTraverseLay(Double traverseLay) { this.traverseLay = traverseLay; }
//
//    public String getNotes() {
//        return notes;
//    }
//
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
//
//    public String getAction() {
//        return action;
//    }
//
//    public void setAction(String action) {
//        this.action = action;
//    }
//
//    @Override public String toString() {
//        return "Assembly{id=" + assemblyId + ", stage='" + stageDescription + "'}";
//    }
//}

package com.etc.pdsapp.model;

import java.math.BigDecimal;

public class Assembly {

    private int assemblyId;                    // IDENTITY, PK
    private String stageDescription;           // NOT NULL
    private int machineId;                     // NOT NULL, FK → machines
    private Integer userId;                    // NULLABLE, FK → users

    private Double lineSpeed;                  // float NULLABLE
    private Double traverseLay;                // float NULLABLE

    private BigDecimal layLength;              // decimal(10,5) NULLABLE
    private BigDecimal pair1LayLength;         // decimal(10,5) NULLABLE
    private BigDecimal pair2LayLength;         // decimal(10,5) NULLABLE
    private BigDecimal pair3LayLength;         // decimal(10,5) NULLABLE
    private BigDecimal pair4LayLength;         // decimal(10,5) NULLABLE

    private String pair1Color;                 // nvarchar(30) NULLABLE
    private String pair2Color;                 // nvarchar(30) NULLABLE
    private String pair3Color;                 // nvarchar(30) NULLABLE
    private String pair4Color;                 // nvarchar(30) NULLABLE

    private String notes;                      // nvarchar(300) NULLABLE
    private String action;                     // Used only in Excel import (INSERT/UPDATE/DELETE)

    // Default constructor

    public Assembly() {
    }

    public Assembly(String stageDesc, int machineId, int userId) {
        this.stageDescription = stageDesc;
        this.machineId = machineId;
        this.userId = userId;
    }

    // Full constructor (used by DAO when reading from DB)
    public Assembly(int assemblyId, String stageDescription, int machineId, Integer userId,
                    Double lineSpeed, Double traverseLay,
                    BigDecimal layLength,
                    BigDecimal pair1LayLength, BigDecimal pair2LayLength,
                    BigDecimal pair3LayLength, BigDecimal pair4LayLength,
                    String pair1Color, String pair2Color, String pair3Color, String pair4Color,
                    String notes) {
        this.assemblyId = assemblyId;
        this.stageDescription = stageDescription;
        this.machineId = machineId;
        this.userId = userId;
        this.lineSpeed = lineSpeed;
        this.traverseLay = traverseLay;
        this.layLength = layLength;
        this.pair1LayLength = pair1LayLength;
        this.pair2LayLength = pair2LayLength;
        this.pair3LayLength = pair3LayLength;
        this.pair4LayLength = pair4LayLength;
        this.pair1Color = pair1Color;
        this.pair2Color = pair2Color;
        this.pair3Color = pair3Color;
        this.pair4Color = pair4Color;
        this.notes = notes;
    }

    // Constructor for Excel import (no ID, action may be set separately)
    public Assembly(String stageDescription, int machineId, Integer userId,
                    Double lineSpeed, Double traverseLay,
                    BigDecimal layLength,
                    BigDecimal pair1LayLength, BigDecimal pair2LayLength,
                    BigDecimal pair3LayLength, BigDecimal pair4LayLength,
                    String pair1Color, String pair2Color, String pair3Color, String pair4Color,
                    String notes) {
        this(0, stageDescription, machineId, userId, lineSpeed, traverseLay,
                layLength, pair1LayLength, pair2LayLength, pair3LayLength, pair4LayLength,
                pair1Color, pair2Color, pair3Color, pair4Color, notes);
    }

    // Getters and Setters
    public int getAssemblyId() { return assemblyId; }
    public void setAssemblyId(int assemblyId) { this.assemblyId = assemblyId; }

    public String getStageDescription() { return stageDescription; }
    public void setStageDescription(String stageDescription) { this.stageDescription = stageDescription; }

    public int getMachineId() { return machineId; }
    public void setMachineId(int machineId) { this.machineId = machineId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Double getLineSpeed() { return lineSpeed; }
    public void setLineSpeed(Double lineSpeed) { this.lineSpeed = lineSpeed; }

    public Double getTraverseLay() { return traverseLay; }
    public void setTraverseLay(Double traverseLay) { this.traverseLay = traverseLay; }

    public BigDecimal getLayLength() { return layLength; }
    public void setLayLength(BigDecimal layLength) { this.layLength = layLength; }

    public BigDecimal getPair1LayLength() { return pair1LayLength; }
    public void setPair1LayLength(BigDecimal pair1LayLength) { this.pair1LayLength = pair1LayLength; }

    public BigDecimal getPair2LayLength() { return pair2LayLength; }
    public void setPair2LayLength(BigDecimal pair2LayLength) { this.pair2LayLength = pair2LayLength; }

    public BigDecimal getPair3LayLength() { return pair3LayLength; }
    public void setPair3LayLength(BigDecimal pair3LayLength) { this.pair3LayLength = pair3LayLength; }

    public BigDecimal getPair4LayLength() { return pair4LayLength; }
    public void setPair4LayLength(BigDecimal pair4LayLength) { this.pair4LayLength = pair4LayLength; }

    public String getPair1Color() { return pair1Color; }
    public void setPair1Color(String pair1Color) { this.pair1Color = pair1Color; }

    public String getPair2Color() { return pair2Color; }
    public void setPair2Color(String pair2Color) { this.pair2Color = pair2Color; }

    public String getPair3Color() { return pair3Color; }
    public void setPair3Color(String pair3Color) { this.pair3Color = pair3Color; }

    public String getPair4Color() { return pair4Color; }
    public void setPair4Color(String pair4Color) { this.pair4Color = pair4Color; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    @Override
    public String toString() {
        return "Assembly{" +
                "assemblyId=" + assemblyId +
                ", stageDescription='" + stageDescription + '\'' +
                ", machineId=" + machineId +
                ", layLength=" + layLength +
                ", pair1Color='" + pair1Color + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
