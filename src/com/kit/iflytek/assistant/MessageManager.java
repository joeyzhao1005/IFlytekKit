package com.kit.iflytek.assistant;

import android.Manifest;
import android.content.Context;

import com.kit.extend.iflytek.R;
import com.kit.iflytek.enums.Service;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.Data;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.iflytek.model.telephone.ContactInfoWapper;
import com.kit.utils.AppUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ResWrapper;
import com.kit.utils.StringUtils;
import com.kit.utils.contact.ContactInfo;
import com.kit.utils.contact.ContactUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhao on 16/7/21.
 */
public class MessageManager {

    private static MessageManager messageManger;


    public static MessageManager getInstance() {
        if (messageManger == null)
            messageManger = new MessageManager();

        return messageManger;
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

            case Operation.SEND:
                return sendMessage(understandResponse);


        }

        return understandResponse;


    }


    /**
     * @param
     * @return Answer 返回类型
     * @Title sendMessage
     * @Description 发送短信
     */
    public static UnderstandResponse sendMessage(UnderstandResponse ur) {
        Answer answer = new Answer();
        answer.type = Answer.Type.TEXT;


        String textStr = "";
        Context context = ResWrapper.getInstance().getContext();

        if (AppUtils.isPermission(context, Manifest.permission.SEND_SMS)
                && AppUtils.isPermission(context, Manifest.permission.READ_CONTACTS)) {
            String name = ur.semantic.getSlots("name", String.class);
            ArrayList<ContactInfo> phones = null;

            if (ContactUtils.isHas(context, name)) {
                phones = ContactUtils.getContactInfoFromName(context,
                        name);
            } else {
                phones = ContactUtils.getContactInfoFromNameLike(context,
                        name);
            }

            if (ListUtils.isNullOrEmpty(phones)) {
                ur.service = Service.CHAT;
                ur.operation = ChatManager.Operation.ANSWER;
                textStr = String.format(ResWrapper.getInstance().getString(
                        R.string.no_find_phone), name);


            } else if (phones.size() == 1) {

                ur.service = Service.MESSAGE;
                ur.operation = Operation.SENDING_CHECK;

                ContactInfo phone = phones.get(0);
                textStr = String.format(ResWrapper.getInstance().getString(
                        R.string.sending_message), name);

                String msgContent = ur.semantic.getSlots("content", String.class);
                if (StringUtils.isEmptyOrNullOrNullStr(msgContent)) {
                    msgContent = "";
                }

                String number = "";
                if (phone != null) {
                    number = phone.getNumber();
                }

                HashMap<String,String> sendingData = new HashMap<String,String>();
                sendingData.put("number",number);
                sendingData.put("content",msgContent);

                ur.data = new Data();
                ur.data.setResult(sendingData);


            } else {

                ArrayList<ContactInfoWapper> contactInfoWappers = new ArrayList<ContactInfoWapper>();
                for (ContactInfo contactInfo : phones) {
                    contactInfoWappers.add(ContactInfoWapper.cast(contactInfo));
                }

                ur.data = new Data();
                ur.data.setResult(contactInfoWappers);

                textStr = ResWrapper.getInstance().getString(R.string.find_some_people);
            }


        } else {
            textStr = context.getString(R.string.no_permission_send_message);
        }


        answer.text = textStr;

        ur.answer = answer;

        return ur;

    }


    public class Operation {


        /**
         * VIEW,表示查看短信,缺省为 VIEW;
         */
        public static final String VIEW = "VIEW";


        /**
         * SYNTH,表示朗读短信。
         */
        public static final String SYNTH = "SYNTH";


        /**
         * 短信内容的类别,取值: 祝福 中秋祝福 国庆祝福 圣诞祝福 元旦祝福 春节祝福 情人节祝 福 元宵节 三八妇女节
         * 愚人节 五一劳动节 青年节 母亲 节 五二零 六一儿童节 端午 节 父亲节 七夕 教师节 万圣 节 感恩节 光棍节
         */
        public static final String QUERY = "QUERY";


        /**
         * 发短信
         */
        public static final String SEND = "SEND";



        /**
         * 发短信检查
         */
        public static final String SENDING_CHECK = "SENDING_CHECK";


        /**
         * 发送联系人、名片
         */
        public static final String SENDCONTACTS = "SENDCONTACTS";


    }


}
