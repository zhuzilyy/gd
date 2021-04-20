package com.gd.form.model;

public class DataUpload {
    private String filename;
    private String filepath;
    private String id;
    private String picturepath;
    private String uploadtype;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath;
    }

    public String getUploadtype() {
        return uploadtype;
    }

    public void setUploadtype(String uploadtype) {
        this.uploadtype = uploadtype;
    }
}
