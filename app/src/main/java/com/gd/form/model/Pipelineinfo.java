package com.gd.form.model;

public class Pipelineinfo {
    /**
     * 管道id
     */
    private Integer id;
    /**
     * 管道名称
     */
    private String name;
    /**
     * 管道描述
     */
    private String  desc;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
