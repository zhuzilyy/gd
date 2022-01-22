package com.gd.form.model;

public class ScoreModel {
    private String departmentname;
    private double totalscore;
    private int departmentid;

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(long totalscore) {
        this.totalscore = totalscore;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }
}
