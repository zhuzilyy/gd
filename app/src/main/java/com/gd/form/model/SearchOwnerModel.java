package com.gd.form.model;

import java.io.Serializable;

public class SearchOwnerModel implements Serializable {
    private double emile;
    private int endstakeid;
    private String endstakename;
    private String id;
    private String ownername;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
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
