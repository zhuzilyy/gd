package com.gd.form.model;

import java.io.Serializable;

public class SearchBuildingModel implements Serializable {
    private int  id;
    private String llegalname;
    private String maintain;
    private int stakeid;
    private int departmentId;
    private String stakename;
    private String select;
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLlegalname() {
        return llegalname;
    }

    public void setLlegalname(String llegalname) {
        this.llegalname = llegalname;
    }

    public String getMaintain() {
        return maintain;
    }

    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }

    public int getStakeid() {
        return stakeid;
    }

    public void setStakeid(int stakeid) {
        this.stakeid = stakeid;
    }

    public String getStakename() {
        return stakename;
    }

    public void setStakename(String stakename) {
        this.stakename = stakename;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }
}

