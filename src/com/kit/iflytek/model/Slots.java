package com.kit.iflytek.model;


import com.kit.utils.GsonUtils;

import java.util.Map;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Semantic
 * @Description 语意
 * @date 2014-7-17 下午3:21:44
 */
public class Slots {

    private IFlyTekLocation location;


    private IFlyTekDateTime datetime;

    /**
     * 内容
     */
    public String content;


    /**
     * 姓名
     */
    public String name;


    public String keyword;


    public IFlyTekLocation getLocation() {
        return location;
    }

    public void setLocation(IFlyTekLocation location) {
        this.location = location;
    }

    public IFlyTekDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(IFlyTekDateTime datetime) {
        this.datetime = datetime;
    }


    public Map<String, Object> dataMap;


}
