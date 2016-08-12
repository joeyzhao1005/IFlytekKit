package com.kit.iflytek.model;

import com.kit.utils.log.ZogUtils;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Answer
 * @Description 问答答复
 * @date 2014-7-17 下午3:22:02
 */

public class Data {

    private Object result;


    @SuppressWarnings("unchecked")
    public <T> T getResult() {
        try {
            return (T) result;
        } catch (Exception e) {
            ZogUtils.showException(e);
            return null;
        }
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
