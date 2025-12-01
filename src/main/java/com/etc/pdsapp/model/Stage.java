package com.etc.pdsapp.model;

public class Stage {
    private int stageId;
    private String stageName;

    public Stage() {
    }
    public Stage(int stageId, String stageName) {
        this.stageId = stageId;
        this.stageName = stageName;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
    public String toString() {
        return stageName;
    }
}
