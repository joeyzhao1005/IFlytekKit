package com.kit.iflytek;

import android.graphics.Bitmap;

import com.kit.config.AppConfig;

/**
 * Created by Zhao on 16/7/26.
 */
public class IFlytekConfig implements AppConfig.IAppConfig {


    @Override
    public Bitmap.Config getPicLevel() {
        return Bitmap.Config.RGB_565;
    }

    @Override
    public Bitmap.Config getPicLevel() {
        return Bitmap.Config.ARGB_4444;
    }

    @Override
    public boolean isShowLog() {
        return false;
    }

    @Override
    public boolean isShowException() {
        return false;
    }

    @Override
    public String getCacheDir() {
        return null;
    }

    @Override
    public String getCacheDataDir() {
        return null;
    }

    @Override
    public String getCacheImageDir() {
        return null;
    }

    public boolean isUseVoice(){
        return false;
    }

}
