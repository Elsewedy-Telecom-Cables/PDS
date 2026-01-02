package com.etc.pds.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {

    @JsonProperty("CODE")
    private String code;

    @JsonProperty("DESC")
    private String description;

    public Ingredient() {}

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return code + " - " + description;
    }


}
