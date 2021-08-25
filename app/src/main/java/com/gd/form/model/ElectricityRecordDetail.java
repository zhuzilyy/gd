package com.gd.form.model;

public class ElectricityRecordDetail {
    private String id;
    private int departmentid;
    private int pipeid;
    private String stakeid;
    private String col1;
    private String col2;
    private String col3;
    private String col4;
    private String col5;
    private CreateTime creatime;
    private String creator;
    private String locate;
    private String weathers;
    private String temperature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public int getPipeid() {
        return pipeid;
    }

    public void setPipeid(int pipeid) {
        this.pipeid = pipeid;
    }

    public String getStakeid() {
        return stakeid;
    }

    public void setStakeid(String stakeid) {
        this.stakeid = stakeid;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

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

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getWeathers() {
        return weathers;
    }

    public void setWeathers(String weathers) {
        this.weathers = weathers;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
