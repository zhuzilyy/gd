package com.gd.form.model;

import java.io.Serializable;

public class KpiModel implements Serializable {
    private String businesstype;
    private CreateTime createdate;
    private String creator;
    private String creatorname;
    private String departmentname;
    private String scorereason;
    private String uploadpicture;
    private int departmentid;
    private long id;
    private int mainflag;
    private long scoreinput;

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public CreateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(CreateTime createdate) {
        this.createdate = createdate;
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

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getScorereason() {
        return scorereason;
    }

    public void setScorereason(String scorereason) {
        this.scorereason = scorereason;
    }

    public String getUploadpicture() {
        return uploadpicture;
    }

    public void setUploadpicture(String uploadpicture) {
        this.uploadpicture = uploadpicture;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMainflag() {
        return mainflag;
    }

    public void setMainflag(int mainflag) {
        this.mainflag = mainflag;
    }

    public long getScoreinput() {
        return scoreinput;
    }

    public void setScoreinput(long scoreinput) {
        this.scoreinput = scoreinput;
    }
}
