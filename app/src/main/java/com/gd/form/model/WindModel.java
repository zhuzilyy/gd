package com.gd.form.model;

import java.io.Serializable;

public class WindModel implements Serializable {
    private String distance;
    private String stakename;
    private int stakeid;
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStakename() {
        return stakename;
    }

    public void setStakename(String stakename) {
        this.stakename = stakename;
    }

    public int getStakeid() {
        return stakeid;
    }

    public void setStakeid(int stakeid) {
        this.stakeid = stakeid;
    }
}
