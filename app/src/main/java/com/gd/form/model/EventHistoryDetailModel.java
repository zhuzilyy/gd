package com.gd.form.model;

public class EventHistoryDetailModel {
    private CreateTime  creatime;
    private String  creator;
    private String  creatorname;
    private String  eventid;
    private String  eventname;
    private String  filename;
    private String  filepath;
    private String  hdcontent;
    private String  picturepath;

    public CreateTime getCreatime() {
        return creatime;
    }

    public void setCreatime(CreateTime creatime) {
        this.creatime = creatime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

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

    public String getHdcontent() {
        return hdcontent;
    }

    public void setHdcontent(String hdcontent) {
        this.hdcontent = hdcontent;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath;
    }
}
