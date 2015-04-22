package com.kit.iflytek;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kit.iflytek.enums.Operation;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.utils.ArrayUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.RandomUtils;

import java.util.ArrayList;

public class DoUnderstandResponse {

    /**
     * 从语义理解返回的结果中解析出UnderstandResponse UnderstandResponse.text即为命令
     * @param res
     * @return
     */
    public static UnderstandResponse getUnderstandResponse(String res) {

        UnderstandResponse obj = null;
        if (res != null && !TextUtils.isEmpty(res)) {

            ZogUtils.printLog(DoUnderstandResponse.class, res);

            Gson gson = new Gson();

            obj = gson.fromJson(res, UnderstandResponse.class);
        }

        ZogUtils.printLog(DoUnderstandResponse.class, obj.text);

        return obj;

    }

    /**
     * 不通过网络，直接匹配到指令
     * @param commondStr
     * @param answerStr
     * @return
     */
    public static UnderstandResponse getLocalUnderstandResponse(String commondStr, String answerStr) {

        UnderstandResponse obj = new UnderstandResponse();
        obj.answer = creatAnswer("T", answerStr);
        obj.text = commondStr;

//            Gson gson = new Gson();
//
//            obj = gson.toJson(obj);
//
//
//        LogUtils.printLog(DoUnderstandResponse.class, obj.text);

        return obj;

    }

    public static Answer getAnAnswer(UnderstandResponse ur,
                                     String defaultAnswerStr) {

        Answer answer = creatAnswer("T", defaultAnswerStr);

        if (ur.operation != null) {
            if (ur.operation.equals(Operation.ANSWER)) {

                if (!ArrayUtils.isNullOrEmpty(ur.moreResults)) {

                    ArrayList<Answer> answerList = new ArrayList<Answer>();
                    answerList.add(ur.answer);

                    for (UnderstandResponse understand : ur.moreResults) {
                        answerList.add(understand.answer);
                    }

                    int position = RandomUtils.getRandomIntNum(0,
                            answerList.size() - 1);

                    ZogUtils.printLog(DoUnderstandResponse.class,
                            "answerList.size():" + answerList.size()
                                    + " position:" + position);


                    answer = answerList.get(position);

                    return answer;

                } else {
                    if (ur.answer != null)
                        return ur.answer;

                }
            }

        }
        return answer;
    }

    public static Answer creatAnswer(String type, String str) {

        Answer answer = new Answer();

        answer.type = type;
        answer.text = str;

        return answer;
    }

}
