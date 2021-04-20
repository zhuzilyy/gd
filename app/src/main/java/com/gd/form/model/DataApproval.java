package com.gd.form.model;

public class DataApproval {
    private String approvalcomment;
    private int approvalresult;
    private String dataformid;
    private String employid;
    private String signfilepath;
    public String getApprovalcomment() {
        return approvalcomment;
    }

    public void setApprovalcomment(String approvalcomment) {
        this.approvalcomment = approvalcomment;
    }

    public int getApprovalresult() {
        return approvalresult;
    }

    public void setApprovalresult(int approvalresult) {
        this.approvalresult = approvalresult;
    }

    public String getDataformid() {
        return dataformid;
    }

    public void setDataformid(String dataformid) {
        this.dataformid = dataformid;
    }

    public String getEmployid() {
        return employid;
    }

    public void setEmployid(String employid) {
        this.employid = employid;
    }

    public String getSignfilepath() {
        return signfilepath;
    }

    public void setSignfilepath(String signfilepath) {
        this.signfilepath = signfilepath;
    }
}
