package com.gd.form.model;

public class MeasureRecordModel {
    private long stakeid;
    private String measuredate;
    private String tester;
    private String pipedeep;
    private String cabledeep;
    private String lockdeep;
    private String resolution;
    public long getStakeid() {
        return stakeid;
    }

    public void setStakeid(long stakeid) {
        this.stakeid = stakeid;
    }

    public String getMeasuredate() {
        return measuredate;
    }

    public void setMeasuredate(String measuredate) {
        this.measuredate = measuredate;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getPipedeep() {
        return pipedeep;
    }

    public void setPipedeep(String pipedeep) {
        this.pipedeep = pipedeep;
    }

    public String getCabledeep() {
        return cabledeep;
    }

    public void setCabledeep(String cabledeep) {
        this.cabledeep = cabledeep;
    }

    public String getLockdeep() {
        return lockdeep;
    }

    public void setLockdeep(String lockdeep) {
        this.lockdeep = lockdeep;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
