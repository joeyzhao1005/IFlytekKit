package com.kit.iflytek.model.telephone;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.kit.iflytek.model.DataWarpper;
import com.kit.utils.ResWrapper;
import com.kit.utils.contact.ContactInfo;

/**
 * Created by Zhao on 16/7/31.
 */
public class TelephoneWarpper implements DataWarpper {

    private ContactInfo contactInfo;


    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }


    @Override
    public String getTitle() {
        return contactInfo.getDisplayName();
    }

    @Override
    public String getContent() {
        return contactInfo.getNumber();
    }


    @Override
    public Drawable getIcon() {
        if (contactInfo.getAvatar() == null)
            return null;

        return new BitmapDrawable(ResWrapper.getInstance().getResouce(), contactInfo.getAvatar());
    }


    public static TelephoneWarpper cast(ContactInfo contactInfo) {
        TelephoneWarpper telephoneWarpper = new TelephoneWarpper();
        telephoneWarpper.setContactInfo(contactInfo);
        return telephoneWarpper;
    }

}
