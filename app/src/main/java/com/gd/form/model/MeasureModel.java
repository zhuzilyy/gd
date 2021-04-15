package com.gd.form.model;

public class MeasureModel {
    private MeasureDateModel measuredate;
    private double cabledeep;
    private double lockdeep;
    private double pipedeep;
    private String resolution;
    private String tester;
    public MeasureDateModel getMeasuredate() {
        return measuredate;
    }

    public void setMeasuredate(MeasureDateModel measuredate) {
        this.measuredate = measuredate;
    }

    public double getCabledeep() {
        return cabledeep;
    }

    public void setCabledeep(double cabledeep) {
        this.cabledeep = cabledeep;
    }

    public double getLockdeep() {
        return lockdeep;
    }

    public void setLockdeep(double lockdeep) {
        this.lockdeep = lockdeep;
    }

    public double getPipedeep() {
        return pipedeep;
    }

    public void setPipedeep(double pipedeep) {
        this.pipedeep = pipedeep;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }
}
