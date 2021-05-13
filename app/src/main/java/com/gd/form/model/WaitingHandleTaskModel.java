package com.gd.form.model;

public class WaitingHandleTaskModel {
    private int id;
    private String creator;
    private String creatorname;
    private String recipient;
    private String recipientname;
    private int departmentid;
    private String departmentname;
    private String finishcontent;
    private String taskcontent;
    private int taskstatus;
    private CreateTime finishtime;
    private CreateTime plantime;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientname() {
        return recipientname;
    }

    public void setRecipientname(String recipientname) {
        this.recipientname = recipientname;
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

    public String getFinishcontent() {
        return finishcontent;
    }

    public void setFinishcontent(String finishcontent) {
        this.finishcontent = finishcontent;
    }

    public String getTaskcontent() {
        return taskcontent;
    }

    public void setTaskcontent(String taskcontent) {
        this.taskcontent = taskcontent;
    }

    public int getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(int taskstatus) {
        this.taskstatus = taskstatus;
    }

    public void setFinishtime(CreateTime finishtime) {
        this.finishtime = finishtime;
    }

    public CreateTime getPlantime() {
        return plantime;
    }

    public void setPlantime(CreateTime plantime) {
        this.plantime = plantime;
    }

    public CreateTime getFinishtime() {
        return finishtime;
    }
}
