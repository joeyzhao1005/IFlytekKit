package com.kit.iflytek.model;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Answer
 * @Description 问答答复
 * @date 2014-7-17 下午3:22:02
 */

public class Answer {

    /**
     * @IsMust true
     * @Describe 显示的类型, 通过这个类型, 可以确定数据的返回内容和客户 端的显示内容: T:text数据 U:url数据
     * TU:text+url数据 IT:image+text数据 ITU:image+text+url数据
     */

    public String type;

    /**
     * @IsMust true
     * @Describe 通用的文字显示, 属于text数据
     */
    public String text;


    /**
     * @IsMust false
     * @Describe 图片的链接地址, 属于image数据
     */
    public String imgUrl;


    /**
     * @IsMust false
     * @Describe 图片的描述文字
     */
    public String imgDesc;


    /**
     * @IsMust false
     * @Describe url链接
     */
    public String url;


    /**
     * @IsMust false
     * @Describe url链接的描述文字
     */
    public String urlDesc;

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
