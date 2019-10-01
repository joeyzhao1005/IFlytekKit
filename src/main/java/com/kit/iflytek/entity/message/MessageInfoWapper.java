package com.kit.iflytek.entity.message;

import android.graphics.drawable.Drawable;

import com.kit.iflytek.entity.DataWarpper;
import com.kit.utils.communication.SMSInfo;

/**
 * Created by Zhao on 16/7/31.
 */
public class MessageInfoWapper implements DataWarpper {

    private SMSInfo smsInfo;


    public SMSInfo getSMSInfo() {
        return smsInfo;
    }

    public void setSMSInfo(SMSInfo smsInfo) {
        this.smsInfo = smsInfo;
    }


    @Override
    public String getTitle() {
        return smsInfo.getPerson();
    }

    @Override
    public String getContent() {
        return smsInfo.getBody();
    }


    @Override
    public Drawable getIcon() {
        return null;
    }


    public static MessageInfoWapper cast(SMSInfo smsInfo) {
        MessageInfoWapper contactInfoWapper = new MessageInfoWapper();
        contactInfoWapper.setSMSInfo(smsInfo);
        return contactInfoWapper;
    }

}
