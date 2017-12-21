package com.kit.iflytek.entity;


import com.kit.utils.GsonUtils;
import com.kit.utils.JsonUtils;
import com.kit.utils.MapUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName Semantic
 * @Description 语意
 * @date 2014-7-17 下午3:21:44
 */
public class Semantic {

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Object getSlots() {
        return slots;
    }

    public void setSlots(Object slots) {
        this.slots = slots;
    }


    public void setSlots(String key, Object value) {
        Map<String, Object> map = null;
        if (slots == null)
            map = new HashMap<String, Object>();
        else {
            try {
                JSONObject jsonObject = new JSONObject(GsonUtils.toJson(slots));
                map = JsonUtils.toMap(jsonObject);
            } catch (Exception e) {
                map = new HashMap<String, Object>();
            }
        }

        map.put(key, value);
        this.slots = map;
    }


    @SuppressWarnings("unchecked")
    public Object getSlots(String key) {
        try {

            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(slots));
            Map<String, Object> rstList = JsonUtils.toMap(jsonObject);

            if (MapUtils.isNullOrEmpty(rstList)) {
                return null;
            }
            Object o = rstList.get(key);

            return rstList.get(key);

        } catch (Exception e) {
            return null;
        }

    }


    public <T> T getSlots(String key, Class<T> clazz) {
        try {
            Object s = getSlots(key);
            return GsonUtils.castObj(s, clazz);
        } catch (Exception e) {
            return null;
        }

    }

    private Object slots;

    private String intent;



//    public <T> Map<?, T> getSlotsMap(String key, Type typeOfT) {
//        try {
//            Object s = getSlots(key);
//            return GsonUtils.castMap(s, typeOfT);
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
//
//    public <T> List<T> getSlotsList(String key, Type typeOfT) {
//        try {
//            Object s = getSlots(key);
//            return GsonUtils.castList(s, typeOfT);
//        } catch (Exception e) {
//            return null;
//        }
//
//    }

}
