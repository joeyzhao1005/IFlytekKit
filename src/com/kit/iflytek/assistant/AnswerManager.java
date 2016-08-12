package com.kit.iflytek.assistant;

import com.kit.iflytek.enums.OperationCommon;
import com.kit.iflytek.enums.Service;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.utils.ArrayUtils;

/**
 * Created by Zhao on 16/7/21.
 */
public class AnswerManager {

    private static AnswerManager answerHelper;

    public static AnswerManager getInstance() {
        if (answerHelper == null)
            answerHelper = new AnswerManager();

        return answerHelper;
    }


    /**
     * 从 Operation.ANSWER中 滤出较为符合聊天习惯的应答(如果没有合适的,直接返回null)
     *
     * @param understandResponses
     * @return
     */
    public UnderstandResponse getBestOneIfUCanAnswer(UnderstandResponse[] understandResponses) {
        if (ArrayUtils.isNullOrEmpty(understandResponses))
            return null;

        UnderstandResponse want = null;
        for (UnderstandResponse u : understandResponses) {
            switch (u.operation) {
                case OperationCommon.ANSWER:
                    switch (u.service) {
                        case Service.CHAT:
                            want = u;
                            return want;

                        case Service.OPEN_QA:
                            want = u;
                            return want;
                    }
            }
        }

        return want;

    }

    /**
     * 从 Operation.ANSWER中 滤出较为符合聊天习惯的应答(如果没有合适的,找一个凑合的)
     *
     * @param understandResponses
     * @return
     */
    public UnderstandResponse getBestOne(UnderstandResponse[] understandResponses) {
        if (ArrayUtils.isNullOrEmpty(understandResponses))
            return null;

        UnderstandResponse want = getBestOneIfUCanAnswer(understandResponses);

        if (want == null)
            want = ArrayUtils.getOne(understandResponses);

        return want;

    }


    public Answer creatAnswer(String str,String type) {

        // LogUtils.i(DoUnderstandResponse.class,
        // "creat default answer");

        Answer answer = new Answer();
        answer.text = str;
        answer.type = type;


        return answer;
    }
}
