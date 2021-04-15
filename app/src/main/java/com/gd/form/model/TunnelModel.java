package com.gd.form.model;

import java.io.Serializable;

public class TunnelModel implements Serializable {
    private double id;
    private double pipeid;
    private double pipelength;
    private String location;
    private String pipename;
    private String pipesituation;
    private String setupmode;
    private String tunneldesc;
    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getPipeid() {
        return pipeid;
    }

    public void setPipeid(double pipeid) {
        this.pipeid = pipeid;
    }

    public double getPipelength() {
        return pipelength;
    }

    public void setPipelength(double pipelength) {
        this.pipelength = pipelength;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPipename() {
        return pipename;
    }

    public void setPipename(String pipename) {
        this.pipename = pipename;
    }

    public String getPipesituation() {
        return pipesituation;
    }

    public void setPipesituation(String pipesituation) {
        this.pipesituation = pipesituation;
    }

    public String getSetupmode() {
        return setupmode;
    }

    public void setSetupmode(String setupmode) {
        this.setupmode = setupmode;
    }

    public String getTunneldesc() {
        return tunneldesc;
    }

    public void setTunneldesc(String tunneldesc) {
        this.tunneldesc = tunneldesc;
    }
}
