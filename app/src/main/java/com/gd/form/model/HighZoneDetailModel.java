package com.gd.form.model;

public class HighZoneDetailModel {
    private HighZoneDetail datadetail;
    private String deptString;
    private String pipeString;
    private String stakeString;
    private DataUpload dataupload;
    private DataApproval datapproval;

    public HighZoneDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(HighZoneDetail datadetail) {
        this.datadetail = datadetail;
    }

    public String getDeptString() {
        return deptString;
    }

    public void setDeptString(String deptString) {
        this.deptString = deptString;
    }

    public String getPipeString() {
        return pipeString;
    }

    public void setPipeString(String pipeString) {
        this.pipeString = pipeString;
    }

    public String getStakeString() {
        return stakeString;
    }

    public void setStakeString(String stakeString) {
        this.stakeString = stakeString;
    }

    public DataUpload getDataupload() {
        return dataupload;
    }

    public void setDataupload(DataUpload dataupload) {
        this.dataupload = dataupload;
    }

    public DataApproval getDatapproval() {
        return datapproval;
    }

    public void setDatapproval(DataApproval datapproval) {
        this.datapproval = datapproval;
    }
}
