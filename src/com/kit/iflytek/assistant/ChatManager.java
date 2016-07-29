package com.kit.iflytek.assistant;

import com.kit.iflytek.model.UnderstandResponse;

/**
 * service为CHAT时候的工具类
 * <p/>
 * Created by Zhao on 16/7/21.
 */
public class ChatManager {


    private static ChatManager chatHelper;

    public static ChatManager getInstance() {
        if (chatHelper == null)
            chatHelper = new ChatManager();

        return chatHelper;
    }

    /**
     * 从 Operation.ANSWER中 滤出较为符合聊天习惯的应答(如果没有合适的,直接返回null)
     *
     * @param understandResponse
     * @return
     */
    public UnderstandResponse dispatch(UnderstandResponse understandResponse) {

        return understandResponse;
    }



    public class Operation {

        /**
         * ANSWER
         * 返回直接问答信息
         */
        public static final String ANSWER = "ANSWER";



    }
}
