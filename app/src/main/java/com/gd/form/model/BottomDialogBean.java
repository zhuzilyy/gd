package com.gd.form.model;

import java.util.List;

/**
 * <p>类描述：底部弹出框bean

 */
public class BottomDialogBean {

    private String title;
    private int type;
    private List<BottomDialogChildBean> list;
    private int numColumns;
    private String rightText;


    //type为3时 是包含单选多选功能 即：如果点击未选中的 则清空所有 选中点击 点击选中 则清空所有
    public BottomDialogBean(String title, int type, List<BottomDialogChildBean> list, int numColumns) {
        this.title = title;
        this.type = type;
        this.list = list;
        this.numColumns=numColumns;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<BottomDialogChildBean> getList() {
        return list;
    }

    public void setList(List<BottomDialogChildBean> list) {
        this.list = list;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }
}
