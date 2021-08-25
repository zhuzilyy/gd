package com.gd.form.model;

public class ElectricityRecordDetailModel {
    private String deptString;
    private String pipeString;
    private String stakeString;
    private ElectricityRecordDetail datadetail;
    private DataApproval datapproval;
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

    public ElectricityRecordDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(ElectricityRecordDetail datadetail) {
        this.datadetail = datadetail;
    }

    public DataApproval getDatapproval() {
        return datapproval;
    }

    public void setDatapproval(DataApproval datapproval) {
        this.datapproval = datapproval;
    }


}
