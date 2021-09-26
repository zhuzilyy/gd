package com.gd.form.model;

public class ProgressModel {
    private String constructionprocess;
    private String processdesc;
    private String projectid;
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
}
