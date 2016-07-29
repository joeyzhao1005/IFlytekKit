package com.kit.iflytek.assistant;

import com.kit.extend.iflytek.R;
import com.kit.iflytek.enums.Service;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.utils.ResWrapper;
import com.kit.utils.StringUtils;

/**
 * Created by Zhao on 16/7/21.
 */
public class WeatherManager {

    private static WeatherManager answerHelper;

    private ILocator locator;

    public static WeatherManager getInstance() {
        if (answerHelper == null)
            answerHelper = new WeatherManager();

        return answerHelper;
    }

    public WeatherManager setLocator(ILocator locator) {
        answerHelper.locator = locator;

        return answerHelper;
    }

    /**
     * 分发天气处理
     *
     * @param understandResponse
     * @return
     */
    @SuppressWarnings("unchecked")
    public UnderstandResponse dispatch(UnderstandResponse understandResponse) {
        if (understandResponse == null)
            return null;

        String replyStr;

        if (StringUtils.isEmptyOrNullOrNullStr(understandResponse.semantic.slots.getLocation().getCityAddr())) {
            //地址为空说明没有言明"城市",需要定位城市,然后自动得出当前城市的天气情况

            if (locator == null)
                return understandResponse;
            else {
                locator.locate();

                understandResponse.service = Service.CHAT;
                understandResponse.operation = ChatManager.Operation.ANSWER;
                replyStr = ResWrapper.getInstance().getString(R.string.no_say_where);
                understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);
            }
        } else {
            switch (understandResponse.operation) {

                case Operation.QUERY:
                    replyStr = String.format(ResWrapper.getInstance().getString(R.string.locate_where)
                            , understandResponse.semantic.slots.getLocation().getCity());
                    understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);

                    break;
            }

        }


        return understandResponse;

    }


    public String getWeatherUrl(UnderstandResponse understandResponses) {

        if (understandResponses == null || understandResponses.webPage == null)
            return null;

        return understandResponses.webPage.getUrl();
    }


    public class Operation {

        public static final String QUERY = "QUERY";


    }

    public interface ILocator {

        String getCity();

        void locate();

    }
}
