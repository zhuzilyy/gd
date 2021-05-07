package com.gd.form.model;

import java.io.Serializable;

public class TunnelModel implements Serializable {
    private int id;
    private int stakeid;
    private int endstakeid;
    private int pipeid;
    private double pipelength;
    private String location;
    private String pipename;
    private String setupmode;
    private String pipesituation;
    private String filename;
    private String uploadfile;
    private String uploadpicture;
    private String linename;
    private String stakename;
    private String endstakename;
    public double getId() {
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

    public int getEndstakeid() {
        return endstakeid;
    }

    public void setEndstakeid(int endstakeid) {
        this.endstakeid = endstakeid;
    }

    public int getPipeid() {
        return pipeid;
    }

    public void setPipeid(int pipeid) {
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

    public String getSetupmode() {
        return setupmode;
    }

    public void setSetupmode(String setupmode) {
        this.setupmode = setupmode;
    }

    public String getPipesituation() {
        return pipesituation;
    }

    public void setPipesituation(String pipesituation) {
        this.pipesituation = pipesituation;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getUploadpicture() {
        return uploadpicture;
    }

    public void setUploadpicture(String uploadpicture) {
        this.uploadpicture = uploadpicture;
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getStakename() {
        return stakename;
    }

    public void setStakename(String stakename) {
        this.stakename = stakename;
    }

    public String getEndstakename() {
        return endstakename;
    }

    public void setEndstakename(String endstakename) {
        this.endstakename = endstakename;
    }
}
