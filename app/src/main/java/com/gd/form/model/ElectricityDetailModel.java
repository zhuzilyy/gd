package com.gd.form.model;

public class ElectricityDetailModel {
    private ElectricityDetail datadetail;
    private DataApproval datapproval;
    private DataUpload dataupload;
    public ElectricityDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(ElectricityDetail datadetail) {
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
}
