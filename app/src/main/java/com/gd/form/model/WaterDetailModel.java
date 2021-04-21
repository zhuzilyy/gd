package com.gd.form.model;

public class WaterDetailModel {
    private WaterDetail datadetail;
    private String deptString;
    private String pipeString;
    private String stakeString;
    private DataApproval datapproval;
    private DataUpload dataupload;
    public WaterDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(WaterDetail datadetail) {
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

    public DataApproval getDatapproval() {
        return datapproval;
    }

    public void setDatapproval(DataApproval datapproval) {
        this.datapproval = datapproval;
    }

    public DataUpload getDataupload() {
        return dataupload;
    }

    public void setDataupload(DataUpload dataupload) {
        this.dataupload = dataupload;
    }
}
