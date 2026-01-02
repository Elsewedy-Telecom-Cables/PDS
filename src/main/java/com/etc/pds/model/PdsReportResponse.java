package com.etc.pds.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PdsReportResponse {

    @JsonProperty("WO")
    private String workOrder;

    @JsonProperty("SO")
    private String salesOrder;

    @JsonProperty("CUST_NAME")
    private String customerName;

    @JsonProperty("SALESREP")
    private String salesRep;

    @JsonProperty("SO_LINE")
    private String soLine;

    @JsonProperty("SO_ITM")
    private String soItemCode;

    @JsonProperty("SO_ITM_DESC")
    private String itemDescription;

    @JsonProperty("SO_ITM_SIZE")
    private String itemSize;

    @JsonProperty("SO_BATCH")
    private String batchNo;

    @JsonProperty("TDS")
    private String tds;

    @JsonProperty("PRD_IRM")
    private String producedItemCode;

    @JsonProperty("PRD_ITM_DESC")
    private String producedItemDescription;

    @JsonProperty("ING")
    private List<Ingredient> ingredients;

    @JsonProperty("Test")
    private List<SpecificationTest> specificationTests;

    // Default constructor (required by Jackson)
    public PdsReportResponse() {}

    // Getters and Setters
    public String getWorkOrder() { return workOrder; }
    public void setWorkOrder(String workOrder) { this.workOrder = workOrder; }

    public String getSalesOrder() { return salesOrder; }
    public void setSalesOrder(String salesOrder) { this.salesOrder = salesOrder; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getSalesRep() { return salesRep; }
    public void setSalesRep(String salesRep) { this.salesRep = salesRep; }

    public String getSoLine() { return soLine; }
    public void setSoLine(String soLine) { this.soLine = soLine; }

    public String getSoItemCode() { return soItemCode; }
    public void setSoItemCode(String soItemCode) { this.soItemCode = soItemCode; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getItemSize() { return itemSize; }
    public void setItemSize(String itemSize) { this.itemSize = itemSize; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public String getTds() { return tds; }
    public void setTds(String tds) { this.tds = tds; }

    public String getProducedItemCode() { return producedItemCode; }

    public void setProducedItemCode(String producedItemCode) { this.producedItemCode = producedItemCode; }

    public String getProducedItemDescription() { return producedItemDescription; }
    public void setProducedItemDescription(String producedItemDescription) { this.producedItemDescription = producedItemDescription; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<SpecificationTest> getSpecificationTests() { return specificationTests; }
    public void setSpecificationTests(List<SpecificationTest> specificationTests) { this.specificationTests = specificationTests; }

    @Override
    public String toString() {
        return "PdsReportResponse{" +
                "workOrder='" + workOrder + '\'' +
                ", itemDescription='" + producedItemDescription + '\'' +
                ", ingredients=" + ingredients +
                ", specificationTests=" + specificationTests +
                '}';
    }

}