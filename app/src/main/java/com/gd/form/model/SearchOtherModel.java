package com.gd.form.model;

import java.io.Serializable;

public class SearchOtherModel implements Serializable {
    private String distance;
    private String name;
    private String stakename;
    private int stakeid;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public int getStakeid() {
        return stakeid;
    }

    public void setStakeid(int stakeid) {
        this.stakeid = stakeid;
    }
}
