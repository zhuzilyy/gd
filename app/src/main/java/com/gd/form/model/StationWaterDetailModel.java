package com.gd.form.model;

public class StationWaterDetailModel {
    private String approvalid;
    private String approvalname;
    private String hydraulicform;
    private String ownerid;
    private String ownername;
    private String stakefrom;
    private int pipeid;
    private int stakeid;
    private String stakename;
    private String waterid;

    public String getWaterid() {
        return waterid;
    }

    public void setWaterid(String waterid) {
        this.waterid = waterid;
    }

    public String getApprovalid() {
        return approvalid;
    }

    public void setApprovalid(String approvalid) {
        this.approvalid = approvalid;
    }

    public String getApprovalname() {
        return approvalname;
    }

    public void setApprovalname(String approvalname) {
        this.approvalname = approvalname;
    }

    public String getHydraulicform() {
        return hydraulicform;
    }

    public void setHydraulicform(String hydraulicform) {
        this.hydraulicform = hydraulicform;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getStakefrom() {
        return stakefrom;
    }

    public void setStakefrom(String stakefrom) {
        this.stakefrom = stakefrom;
    }

    public int getPipeid() {
        return pipeid;
    }

    public void setPipeid(int pipeid) {
        this.pipeid = pipeid;
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

    @Override
    public String toString() {
        return "StationWaterDetailModel{" +
                "approvalid='" + approvalid + '\'' +
                ", approvalname='" + approvalname + '\'' +
                ", hydraulicform='" + hydraulicform + '\'' +
                ", ownerid='" + ownerid + '\'' +
                ", ownername='" + ownername + '\'' +
                ", stakefrom='" + stakefrom + '\'' +
                ", pipeid=" + pipeid +
                ", stakeid=" + stakeid +
                '}';
    }
}
