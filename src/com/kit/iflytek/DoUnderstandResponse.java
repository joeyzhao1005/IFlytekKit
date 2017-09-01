package com.kit.iflytek;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.iflytek.cloud.UnderstanderResult;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.utils.log.ZogUtils;

public class DoUnderstandResponse {

    /**
     * 从语义理解返回的结果中解析出UnderstandResponse UnderstandResponse.text即为命令
     *
     * @return
     */
    public static UnderstandResponse getUnderstandResponse(UnderstanderResult result) {

        if (result == null)
            return null;

        String res = result.getResultString();

        UnderstandResponse obj = null;
        if (res != null && !TextUtils.isEmpty(res)) {

            ZogUtils.i(res);

            Gson gson = new Gson();

            try {
                obj = gson.fromJson(res, UnderstandResponse.class);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }
        }


        return obj;

    }


    /**
     * 不通过网络，直接匹配到指令
     *
     * @param commondStr
     * @param answerStr
     * @return
     */
    public static UnderstandResponse getLocalUnderstandResponse(String commondStr, String answerStr) {

        UnderstandResponse obj = new UnderstandResponse();
        obj.setAnswer(creatAnswer("T", answerStr));
        obj.setText(commondStr);

//            Gson gson = new Gson();
//
//            obj = gson.toJson(obj);
//
//
//        LogUtils.i(DoUnderstandResponse.class, obj.text);

        return obj;

    }


    public static Answer creatAnswer(String type, String str) {

        Answer answer = new Answer();

        answer.setType(type);
        answer.setText(str);

        return answer;
    }

}
