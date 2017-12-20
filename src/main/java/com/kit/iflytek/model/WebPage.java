package com.kit.iflytek.model;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Answer
 * @Description 问答答复
 * @date 2014-7-17 下午3:22:02
 */

public class WebPage {
    /**
     * header
     * String
     * <p/>
     * 否
     * <p/>
     * <p/>
     * 导语部分
     */
    private String header;


    /**
     * headerTts
     * String
     * 否
     * 导语播报内容,若字段不存在,则 取值与header相同
     */
    private String headerTts;


    /**
     * url
     * String
     * <p/>
     * 是
     * <p/>
     * <p/>
     * 对data进行UI展示的链接
     */
    private String url;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeaderTts() {
        return headerTts;
    }

    public void setHeaderTts(String headerTts) {
        this.headerTts = headerTts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
