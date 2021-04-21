package com.gd.form.model;

public class BuildingDetailModel {
    private BuildingDetail datadetail;
    private String pipeString;
    private String stakeString;
    private DataApproval datapproval;
    private DataUpload dataupload;

    public BuildingDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(BuildingDetail datadetail) {
        this.datadetail = datadetail;
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
