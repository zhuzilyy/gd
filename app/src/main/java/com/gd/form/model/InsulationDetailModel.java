package com.gd.form.model;

public class InsulationDetailModel {
    private InsulationDetail datadetail;
    private DataApproval datapproval;
    private DataUpload dataupload;
    private String deptString;
    private String pipeString;
    public InsulationDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(InsulationDetail datadetail) {
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
}
