package com.elsewedyt.pdsapp.models;

public class Assembly {
    private int assemblyId;                 // IDENTITY
    private String stageDescription;        // NOT NULL
    private int machineId;                  // NOT NULL (FK)
    private Integer userId;                 // NULLABLE (FK)
    private Double lineSpeed;               // NULLABLE (float)
    private Double traverseLay;             // NULLABLE (float)
    private String notes ;
    private String action ;

    public Assembly() {}

    public Assembly(int assemblyId, String stageDescription, int machineId, Integer userId,
                    Double lineSpeed, Double traverseLay,String notes) {
        this.assemblyId = assemblyId;
        this.stageDescription = stageDescription;
        this.machineId = machineId;
        this.userId = userId;
        this.lineSpeed = lineSpeed;
        this.traverseLay = traverseLay;
        this.notes = notes ;
    }

    // For Creation Without ID For Import  Excel
    public Assembly(String stageDescription, int machineId, Integer userId,
                    Double lineSpeed, Double traverseLay,String notes) {
        this(0, stageDescription, machineId, userId, lineSpeed, traverseLay,notes);
    }

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override public String toString() {
        return "Assembly{id=" + assemblyId + ", stage='" + stageDescription + "'}";
    }
}
