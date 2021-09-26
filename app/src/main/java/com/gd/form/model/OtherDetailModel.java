package com.gd.form.model;

public class OtherDetailModel {
    private CreateTime creatime;
    private String creator;
    private String id;
    private int othertype;
    private String perfectcondition;
    private int pipeid;
    private String stakefrom;
    private int stakeid;
    private String stakename;
    private String remark;
    private String uploadpicture;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOthertype() {
        return othertype;
    }

    public void setOthertype(int othertype) {
        this.othertype = othertype;
    }

    public String getPerfectcondition() {
        return perfectcondition;
    }

    public void setPerfectcondition(String perfectcondition) {
        this.perfectcondition = perfectcondition;
    }

    public int getPipeid() {
        return pipeid;
    }

    public void setPipeid(int pipeid) {
        this.pipeid = pipeid;
    }

    public String getStakefrom() {
        return stakefrom;
    }

    public void setStakefrom(String stakefrom) {
        this.stakefrom = stakefrom;
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

    public String getUploadpicture() {
        return uploadpicture;
    }

    public void setUploadpicture(String uploadpicture) {
        this.uploadpicture = uploadpicture;
    }

    public CreateTime getCreatime() {
        return creatime;
    }

    public void setCreatime(CreateTime creatime) {
        this.creatime = creatime;
    }
}
