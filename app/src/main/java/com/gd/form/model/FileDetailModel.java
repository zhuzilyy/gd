package com.gd.form.model;

public class FileDetailModel {
    private String creator;
    private String filename;
    private String filetype;
    private long id;
    private String uploadfile;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }
}
