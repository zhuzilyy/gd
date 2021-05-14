package com.gd.form.model;

public class UpdateModel {
    private String appversion;
    private String downloadpath;
    private String name;
    private String updatecomment;
    private int id;
    private int versioncode;

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatecomment() {
        return updatecomment;
    }

    public void setUpdatecomment(String updatecomment) {
        this.updatecomment = updatecomment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }
}
