package com.gd.form.model;

import java.io.Serializable;

public class SearchPipeInfoModel implements Serializable {
    private double emile;
    private int endstakeid;
    private String endstakename;
    private int id;
    private String maintain;
    private String pipename;
    private double smile;
    private int stakeid;
    private String stakename;
    public double getEmile() {
        return emile;
    }

    public void setEmile(double emile) {
        this.emile = emile;
    }

    public int getEndstakeid() {
        return endstakeid;
    }

    public void setEndstakeid(int endstakeid) {
        this.endstakeid = endstakeid;
    }

    public String getEndstakename() {
        return endstakename;
    }

    public void setEndstakename(String endstakename) {
        this.endstakename = endstakename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaintain() {
        return maintain;
    }

    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }

    public String getPipename() {
        return pipename;
    }

    public void setPipename(String pipename) {
        this.pipename = pipename;
    }

    public double getSmile() {
        return smile;
    }

    public void setSmile(double smile) {
        this.smile = smile;
    }

    public int getStakeid() {
        return stakeid;
    }

    public void setStakeid(int stakeid) {
        this.stakeid = stakeid;
    }

    public String getStakename() {
        return stakename;
    }

    public void setStakename(String stakename) {
        this.stakename = stakename;
    }
}
