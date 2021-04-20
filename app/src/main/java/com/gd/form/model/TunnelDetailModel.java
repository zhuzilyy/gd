package com.gd.form.model;

public class TunnelDetailModel {
    private TunnelDataDetail datadetail;
    private DataApproval datapproval;
    private DataUpload dataupload;
    private String deptString;
    private String pipeString;
    private String stakeString;

    public TunnelDataDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(TunnelDataDetail datadetail) {
        this.datadetail = datadetail;
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
}
