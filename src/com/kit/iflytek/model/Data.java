package com.kit.iflytek.model;

import com.kit.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Answer
 * @Description 问答答复
 * @date 2014-7-17 下午3:22:02
 */

public class Data {

    private Object result;


    /**
     * 解析成list
     *
     * @return
     */
    public <T> ArrayList<T> getResult(Type typeOfT) {
        return GsonUtils.getArrayList(GsonUtils.toJson(result), typeOfT);
    }


    public <T> T getResult(Class<T> clazz) {
        return GsonUtils.getObj(GsonUtils.toJson(result), clazz);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


//    public String getResult() {
//        return GsonUtils.toJson(result);
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
}
