package com.gd.form.model;

import java.io.Serializable;

public class EaluationPicBean implements Serializable {
    public int height;
    public int width;
    public int x;
    public int y;
    public int attachmentId;
    public String imageId;
    //        原图
    public String imageUrl;
    //        缩略图
    public String smallImageUrl;

    @Override
    public String toString() {
        return "EaluationPicBean{" +
                "attachmentId=" + attachmentId +
                ", imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", smallImageUrl='" + smallImageUrl + '\'' +
                '}';
    }
}
