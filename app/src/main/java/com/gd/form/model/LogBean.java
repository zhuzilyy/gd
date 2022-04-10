package com.gd.form.model;

public class LogBean {
    private CreateTime creatime;
    private String creator;
    private String creatorname;
    private String dailycontent;
    private int departmentid;
    private int mainflag;
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

    public String getDailycontent() {
        return dailycontent;
    }

    public void setDailycontent(String dailycontent) {
        this.dailycontent = dailycontent;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public int getMainflag() {
        return mainflag;
    }

    public void setMainflag(int mainflag) {
        this.mainflag = mainflag;
    }
}
