package com.kit.iflytek.entity;

/**
 * Created by Zhao on 16/7/19.
 */
public class IFlyTekLocation {

//    "cityAddr": "上海",
//            "city": "上海市",
//            "type": "LOC_BASIC"


    private String cityAddr;
    private String city;
    private String type;

    public String getCityAddr() {
        return cityAddr;
    }

    public void setCityAddr(String cityAddr) {
        this.cityAddr = cityAddr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
