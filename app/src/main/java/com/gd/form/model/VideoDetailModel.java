package com.gd.form.model;

public class VideoDetailModel {
    private VideoDetail datadetail;
    private String pipeString;
    private DataApproval datapproval;
    private DataUpload dataupload;
    public VideoDetail getDatadetail() {
        return datadetail;
    }

    public void setDatadetail(VideoDetail datadetail) {
        this.datadetail = datadetail;
    }

    public String getPipeString() {
        return pipeString;
    }

    public void setPipeString(String pipeString) {
        this.pipeString = pipeString;
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
