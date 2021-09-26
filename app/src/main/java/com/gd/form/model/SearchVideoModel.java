package com.gd.form.model;

import java.io.Serializable;

public class SearchVideoModel implements Serializable {
    private String distance;
    private int stakeid;
    private String stakename;
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
