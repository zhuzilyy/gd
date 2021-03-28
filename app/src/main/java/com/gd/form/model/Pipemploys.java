package com.gd.form.model;

public class Pipemploys
{
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 岗位职责id
     */
    private Integer professionalid;
    /**
     * 用户部门id
     */
    private Integer departmentid;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 角色id
     */
    private Integer roleid;
    /**
     * 电话
     */
    private String telenumber;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getProfessionalid() {
        return professionalid;
    }
    public void setProfessionalid(Integer professionalid) {
        this.professionalid = professionalid;
    }
    public Integer getDepartmentid() {
        return departmentid;
    }
    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public Integer getRoleid() {
        return roleid;
    }
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
    public String getTelenumber() {
        return telenumber;
    }
    public void setTelenumber(String telenumber) {
        this.telenumber = telenumber;
    }

}
