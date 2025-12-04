package com.etc.pdsapp.model;

public class Machine {
    private int machineId ;
    private String machineName;
    private Integer stageId;

    public Machine(int machine_id, String machineName,Integer stageId) {
        this.machineId = machine_id;
        this.machineName = machineName;
        this.stageId = stageId ;
    }

    public Machine() {

    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }


    @Override
    public String toString() {
        return machineName ;
    }
}
