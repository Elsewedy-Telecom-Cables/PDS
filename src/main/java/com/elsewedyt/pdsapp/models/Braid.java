package com.elsewedyt.pdsapp.models;

public class Braid {
    private int braidId;                    // IDENTITY
    private String stageDescription;        // NOT NULL
    private int machineId;                  // NOT NULL
    private Integer userId;                 // NULLABLE
    private Double deckSpeed;               // NULLABLE
    private Double speed;                   // NULLABLE
    private Double pitch;                   // NULLABLE
    private String notes ;
    private String action ;

    public Braid() {}

    public Braid(int braidId, String stageDescription, int machineId, Integer userId,
                 Double deckSpeed, Double speed, Double pitch,String notes) {
        this.braidId = braidId;
        this.stageDescription = stageDescription;
        this.machineId = machineId;
        this.userId = userId;
        this.deckSpeed = deckSpeed;
        this.speed = speed;
        this.pitch = pitch;
        this.notes = notes;
    }
    // For Creation Without ID For Import  Excel
    public Braid(String stageDescription, int machineId, Integer userId,
                 Double deckSpeed, Double speed, Double pitch,String notes) {
        this(0, stageDescription, machineId, userId, deckSpeed, speed, pitch ,notes);
    }

    public int getBraidId() { return braidId; }
    public void setBraidId(int braidId) { this.braidId = braidId; }

    public String getStageDescription() { return stageDescription; }
    public void setStageDescription(String stageDescription) { this.stageDescription = stageDescription; }

    public int getMachineId() { return machineId; }
    public void setMachineId(int machineId) { this.machineId = machineId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Double getDeckSpeed() { return deckSpeed; }
    public void setDeckSpeed(Double deckSpeed) { this.deckSpeed = deckSpeed; }

    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }

    public Double getPitch() { return pitch; }
    public void setPitch(Double pitch) { this.pitch = pitch; }

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
        return "Braid{id=" + braidId + ", stage='" + stageDescription + "'}";
    }
}
