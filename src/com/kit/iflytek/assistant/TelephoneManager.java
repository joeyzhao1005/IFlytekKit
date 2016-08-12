package com.kit.iflytek.assistant;

import android.Manifest;
import android.content.Context;

import com.kit.extend.iflytek.R;
import com.kit.iflytek.enums.Service;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.Data;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.iflytek.model.telephone.TelephoneWarpper;
import com.kit.utils.AppUtils;
import com.kit.utils.CallAndSmsUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ResWrapper;
import com.kit.utils.StringUtils;
import com.kit.utils.contact.ContactInfo;
import com.kit.utils.contact.ContactUtils;

import java.util.ArrayList;

/**
 * Created by Zhao on 16/7/21.
 */
public class TelephoneManager {

    private static TelephoneManager telephone;


    public static TelephoneManager getInstance() {
        if (telephone == null)
            telephone = new TelephoneManager();

        return telephone;
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

            case Operation.CALL:
                return callPhone(understandResponse);


        }

        return understandResponse;


    }


    /**
     * 拨打电话
     */
    public static UnderstandResponse callPhone(UnderstandResponse ur) {
        Answer answer = new Answer();
        answer.type = Answer.Type.TEXT;

        String textStr = "";
        Context context = ResWrapper.getInstance().getContext();
        if (AppUtils.isPermission(context, Manifest.permission.CALL_PHONE)
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

                ur.service = Service.CHAT;
                ur.operation = ChatManager.Operation.ANSWER;

                ContactInfo phone = phones.get(0);
                textStr = String.format(ResWrapper.getInstance().getString(
                        R.string.calling), name);

                if (phone != null && !StringUtils.isEmptyOrNullOrNullStr(phone.getNumber()))
                    CallAndSmsUtils.mkCall(context, phone.getNumber());

            } else {

                ArrayList<TelephoneWarpper> telephoneWarppers = new ArrayList<TelephoneWarpper>();
                for (ContactInfo contactInfo : phones) {
                    telephoneWarppers.add(TelephoneWarpper.cast(contactInfo));
                }
                ur.data = new Data();
                ur.data.setResult(telephoneWarppers);

                textStr = ResWrapper.getInstance().getString(R.string.find_some_people);
            }


        } else {
            textStr = context.getString(R.string.no_permission_call_phone);
        }


        answer.text = textStr;

        ur.answer = answer;

        return ur;
    }


    public class Operation {

        /**
         * 打电话
         */
        public static final String CALL = "CALL";


        /**
         * 查看通话记录
         */
        public static final String VIEW = "VIEW";

    }


}
