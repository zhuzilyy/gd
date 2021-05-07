package com.gd.form.model;

import java.io.Serializable;

public class WaterModel implements Serializable {
    private String distance;
    private String maintain;
    private String name;
    private String stakename;
    private double stakeform;
    private int id;
    private int stakeid;
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMaintain() {
        return maintain;
    }

    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStakename() {
        return stakename;
    }

    public void setStakename(String stakename) {
        this.stakename = stakename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStakeid() {
        return stakeid;
    }

    public void setStakeid(int stakeid) {
        this.stakeid = stakeid;
    }
    public double getStakeform() {
        return stakeform;
    }

    public void setStakeform(double stakeform) {
        this.stakeform = stakeform;
    }

    @Override
    public String toString() {
        return "WaterModel{" +
                "distance='" + distance + '\'' +
                ", maintain='" + maintain + '\'' +
                ", name='" + name + '\'' +
                ", stakename='" + stakename + '\'' +
                ", id=" + id +
                ", stakeid=" + stakeid +
                '}';
    }
}
