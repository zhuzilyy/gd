package com.gd.form.model;

/**
 * <p>类描述：底部弹出框子Bean
 */
public class BottomDialogChildBean {

    private int imgId;
    private String text;
    private String type;
    private int level;
    private boolean isCheck;



    public BottomDialogChildBean(int imgId, String text) {
        this.imgId = imgId;
        this.text = text;
    }

    public BottomDialogChildBean(String text, boolean isCheck, String type) {
        this.text = text;
        this.isCheck = isCheck;
        this.type=type;
    }

    public BottomDialogChildBean(String text, boolean isCheck, int level) {
        this.text = text;
        this.isCheck = isCheck;
        this.level = level;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
