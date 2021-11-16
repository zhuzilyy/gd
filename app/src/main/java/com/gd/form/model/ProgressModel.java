package com.gd.form.model;

public class ProgressModel {
    private String constructionprocess;
    private String processdesc;
    private String projectid;
    private String filename;
    private String uploadfile;
    private String uploadpicture;
    private CreateTime recorddate;

    public String getConstructionprocess() {
        return constructionprocess;
    }

    public void setConstructionprocess(String constructionprocess) {
        this.constructionprocess = constructionprocess;
    }

    public String getProcessdesc() {
        return processdesc;
    }

    public void setProcessdesc(String processdesc) {
        this.processdesc = processdesc;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public CreateTime getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(CreateTime recorddate) {
        this.recorddate = recorddate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getUploadpicture() {
        return uploadpicture;
    }

    public void setUploadpicture(String uploadpicture) {
        this.uploadpicture = uploadpicture;
    }
}
