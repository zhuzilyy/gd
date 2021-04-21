package com.gd.form.model;

public class DeviceDetailModel {
    private DeviceDetail datadetail;
    private String deptString;
    private String pipeString;
    private String stakeString;
    private DataUpload dataupload;
    private DataApproval datapproval;
    public DeviceDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(DeviceDetail datadetail) {
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
