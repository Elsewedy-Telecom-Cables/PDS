package com.elsewedyt.pdsapp.models;

public class Machine {
    private int machineId ;
    private String machineName;

    public Machine(int machine_id, String machineName) {
        this.machineId = machine_id;
        this.machineName = machineName;
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

    @Override
    public String toString() {
        return machineName ;
    }
}
