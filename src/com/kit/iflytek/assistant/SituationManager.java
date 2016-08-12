package com.kit.iflytek.assistant;

import com.kit.iflytek.model.UnderstandResponse;

/**
 * Created by Zhao on 16/7/21.
 */
public class SituationManager {

    private static SituationManager situationManager;


    public static SituationManager getInstance() {
        if (situationManager == null)
            situationManager = new SituationManager();

        return situationManager;
    }


    /**
     * 分发音乐
     *
     * @param understandResponse
     * @return
     */
    @SuppressWarnings("unchecked")
    public UnderstandResponse dispatch(UnderstandResponse understandResponse) {
        if (understandResponse == null)
            return null;


        switch (understandResponse.operation) {

            case Operation.LAUNCH:

                understandResponse =  AppManager.getInstance().dispatch(understandResponse);
                break;

            case Operation.SHOW_LIST:
                return understandResponse;

        }


        return understandResponse;

    }


    public class Operation {

        public static final String PLAY = "PLAY";


        /**
         * LAUNCH
         * 打开应用(缺省)
         */
        public static final String SHOW_LIST = "SHOW_LIST";

        /**
         * LAUNCH
         * 打开应用(缺省)
         */
        public static final String LAUNCH = "LAUNCH";

        /**
         * 反问
         */
        public static final String ASK_BACK = "ASK_BACK";


    }


}
