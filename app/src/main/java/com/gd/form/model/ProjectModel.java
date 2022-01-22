package com.gd.form.model;

public class ProjectModel {
    private int  departmentid;
    private String departmentname;
    private String constructionprocess;
    private String projectid;
    private String projectname;
    private int projectstatus;
    private String pipedistance;

    public String getPipedistance() {
        return pipedistance;
    }

    public void setPipedistance(String pipedistance) {
        this.pipedistance = pipedistance;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getConstructionprocess() {
        return constructionprocess;
    }

    public void setConstructionprocess(String constructionprocess) {
        this.constructionprocess = constructionprocess;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public int getProjectstatus() {
        return projectstatus;
    }

    public void setProjectstatus(int projectstatus) {
        this.projectstatus = projectstatus;
    }
}
