package com.etc.pds.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecificationTest {

    @JsonProperty("SEQ")
    private String sequence;

    @JsonProperty("TEST_DESC")
    private String testDescription;

    @JsonProperty("MIN_VAL")
    private String minValue;

    @JsonProperty("TARGET_VAL")
    private String targetValue;

    @JsonProperty("MAX_VAL")
    private String maxValue;

    @JsonProperty("COMMENT")
    private String comment;

    public SpecificationTest() {}

    public String getSequence() { return sequence; }
    public void setSequence(String sequence) { this.sequence = sequence; }

    public String getTestDescription() { return testDescription; }
    public void setTestDescription(String testDescription) { this.testDescription = testDescription; }

    public String getMinValue() { return minValue; }
    public void setMinValue(String minValue) { this.minValue = minValue; }

    public String getTargetValue() { return targetValue; }
    public void setTargetValue(String targetValue) { this.targetValue = targetValue; }

    public String getMaxValue() { return maxValue; }

    public void setMaxValue(String maxValue) { this.maxValue = maxValue; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return testDescription +
                " | Target: " + targetValue +
                " | Range: [" + minValue + " - " + maxValue + "]" +
                (comment != null ? " | " + comment : "");
    }
}