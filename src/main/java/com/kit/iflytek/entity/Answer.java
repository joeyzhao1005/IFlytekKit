package com.kit.iflytek.entity;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Answer
 * @Description 问答答复
 * @date 2014-7-17 下午3:22:02
 */

public class Answer {


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlDesc() {
        return urlDesc;
    }

    public void setUrlDesc(String urlDesc) {
        this.urlDesc = urlDesc;
    }

    /**
     * @IsMust true
     * @Describe 显示的类型, 通过这个类型, 可以确定数据的返回内容和客户 端的显示内容: T:text数据 U:url数据
     * TU:text+url数据 IT:image+text数据 ITU:image+text+url数据
     */

    private String type;

    /**
     * @IsMust true
     * @Describe 通用的文字显示, 属于text数据
     */
    private String text;


    /**
     * @IsMust false
     * @Describe 图片的链接地址, 属于image数据
     */
    private String imgUrl;


    /**
     * @IsMust false
     * @Describe 图片的描述文字
     */
    private String imgDesc;


    /**
     * @IsMust false
     * @Describe url链接
     */
    private String url;


    /**
     * @IsMust false
     * @Describe url链接的描述文字
     */
    private String urlDesc;


    public static class Type{
        public static final String TEXT = "T";
        public static final String URL = "U";
        public static final String TEXT_URL = "TU";
        public static final String IMAGE_TEXT = "IT";
        public static final String IMAGE_TEXT_URL = "ITU";

    }

    @Override
    public String toString() {
        return "Answer{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgDesc='" + imgDesc + '\'' +
                ", url='" + url + '\'' +
                ", urlDesc='" + urlDesc + '\'' +
                '}';
    }
}
