package com.etc.pds.model;
public class OracleIntegration {
    private OracleIntegration workOrder;
    private String machine ;
    private String machine_step ;
    private String planned_date ;
    private String cable_code ;
    private String planned_required_length ;
    private String operation ;
    private String description ;
    private String planned_speed ;
    private String soBatch ;   //sub_work_order

    private String wo;   //work_order
    private String organization_name;   // organization_name
    private String item_size;   // cable_size
    private String item;   // cable_code
    private String item_desc;   // cableDescription
    private String item_tds;  // tds_num

    public OracleIntegration() {
    }

    public OracleIntegration(String wo, String item_size, String organization_name, String item, String item_desc, String item_tds) {
        this.wo = wo;
        this.item_size = item_size;
        this.organization_name = organization_name;
        this.item = item;
        this.item_desc = item_desc;
        this.item_tds = item_tds;
    }

    public OracleIntegration(OracleIntegration workOrder, String machine, String machine_step, String planned_date,
                             String cable_code, String planned_required_length, String operation, String description,
                             String planned_speed, String soBatch, String wo, String organization_name, String item_size,
                             String item, String item_desc, String item_tds) {
        this.workOrder = workOrder;
        this.machine = machine;
        this.machine_step = machine_step;
        this.planned_date = planned_date;
        this.cable_code = cable_code;
        this.planned_required_length = planned_required_length;
        this.operation = operation;
        this.description = description;
        this.planned_speed = planned_speed;
        this.soBatch = soBatch;
        this.wo = wo;
        this.organization_name = organization_name;
        this.item_size = item_size;
        this.item = item;
        this.item_desc = item_desc;
        this.item_tds = item_tds;
    }


    public String getWo() {
        return wo;
    }

    public void setWo(String wo) {
        this.wo = wo;
    }

    public String getItem_size() {
        return item_size;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_tds() {
        return item_tds;
    }

    public void setItem_tds(String item_tds) {
        this.item_tds = item_tds;
    }


    public OracleIntegration getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(OracleIntegration workOrder) {
        this.workOrder = workOrder;
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

    @Override
    public String toString() {
        return "OracleIntegration{" +
                "workOrder=" + workOrder +
                ", machine='" + machine + '\'' +
                ", machine_step='" + machine_step + '\'' +
                ", planned_date='" + planned_date + '\'' +
                ", cable_code='" + cable_code + '\'' +
                ", planned_required_length='" + planned_required_length + '\'' +
                ", operation='" + operation + '\'' +
                ", description='" + description + '\'' +
                ", planned_speed='" + planned_speed + '\'' +
                ", soBatch='" + soBatch + '\'' +
                ", wo='" + wo + '\'' +
                ", organization_name='" + organization_name + '\'' +
                ", item_size='" + item_size + '\'' +
                ", item='" + item + '\'' +
                ", item_desc='" + item_desc + '\'' +
                ", item_tds='" + item_tds + '\'' +
                '}';
    }
}
