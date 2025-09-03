package com.elsewedyt.pdsapp.models;

public class OracleWorkOrder {
    private String machine ;
    private String machine_step ;
    private String planned_date ;
    private String cable_code ;
    private String planned_required_length ;
    private String operation ;
    private String description ;
    private String planned_speed ;
    private String soBatch ;
    private String workOrder ;
    private String tdsNumber ;
    private String cableSize ;
    private String cableCode ;
    private String cableDescription ;

    public OracleWorkOrder() {
    }

    public OracleWorkOrder(String workOrder, String tdsNumber, String cableSize, String cableCode, String cableDescription) {
        this.workOrder = workOrder;
        this.tdsNumber = tdsNumber;
        this.cableSize = cableSize;
        this.cableCode = cableCode;
        this.cableDescription = cableDescription;
    }

    public String getCableCode() {
        return cableCode;
    }

    public void setCableCode(String cableCode) {
        this.cableCode = cableCode;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public String getTdsNumber() {
        return tdsNumber;
    }

    public void setTdsNumber(String tdsNumber) {
        this.tdsNumber = tdsNumber;
    }

    public String getCableSize() {
        return cableSize;
    }

    public void setCableSize(String cableSize) {
        this.cableSize = cableSize;
    }

    public String getCableDescription() {
        return cableDescription;
    }

    public void setCableDescription(String cableDescription) {
        this.cableDescription = cableDescription;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getMachine_step() {
        return machine_step;
    }

    public void setMachine_step(String machine_step) {
        this.machine_step = machine_step;
    }

    public String getPlanned_date() {
        return planned_date;
    }

    public void setPlanned_date(String planned_date) {
        this.planned_date = planned_date;
    }

    public String getCable_code() {
        return cable_code;
    }

    public void setCable_code(String cable_code) {
        this.cable_code = cable_code;
    }

    public String getPlanned_required_length() {
        return planned_required_length;
    }

    public void setPlanned_required_length(String planned_required_length) {
        this.planned_required_length = planned_required_length;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanned_speed() {
        return planned_speed;
    }

    public void setPlanned_speed(String planned_speed) {
        this.planned_speed = planned_speed;
    }

    public String getSoBatch() {
        return soBatch;
    }

    public void setSoBatch(String soBatch) {
        this.soBatch = soBatch;
    }
}
