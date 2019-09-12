package com.zero.egg.tool;

import java.io.InputStream;

/**
 * @ClassName ImageHolder
 * @Description  封装图片inputStream和图片名称
 * @Author lyming
 * @Date 2019/9/11 10:18 上午
 **/
public class ImageHolder {
    /**
     * 图片名
     */
    private String imageName;

    /**
     * 图片流
     */
    private InputStream image;

    public ImageHolder(String imageName, InputStream image) {
        this.imageName = imageName;
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }
}
