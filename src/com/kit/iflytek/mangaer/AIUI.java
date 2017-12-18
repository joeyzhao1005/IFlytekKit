package com.kit.iflytek.mangaer;

import android.app.Activity;
import android.app.Application;
import android.content.res.AssetManager;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.SpeechUtility;
import com.kit.utils.ResWrapper;
import com.kit.utils.log.ZogUtils;

import java.io.IOException;
import java.io.InputStream;


public class AIUI {


    public void listen() {
        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
        mAIUIAgent.sendMessage(wakeupMsg);
    }


    /**
     * 开始语音理解
     */
    public AIUI startVoiceNlp() {
        synchronized (AIUI.class) {

            ZogUtils.i("start voice nlp");

            if (!checkAIUIAgent()) {
                return mInstance;
            }

            // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
            // 默认为oneshot 模式，即一次唤醒后就进入休眠，如果语音唤醒后，需要进行文本语义，请将改段逻辑copy至startTextNlp()开头处
            if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
                AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
                mAIUIAgent.sendMessage(wakeupMsg);
            }

            // 打开AIUI内部录音机，开始录音
            String params = "sample_rate=16000,data_type=audio";
            AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null);
            mAIUIAgent.sendMessage(writeMsg);
            return mInstance;
        }
    }


    /**
     * 停止语音理解
     */
    public AIUI AIUIListener(AIUIListener aiuiListener) {
        this.mAIUIListener = aiuiListener;
        return this;
    }


    /**
     * 停止语音理解
     */
    public void stopVoiceNlp() {
        synchronized (AIUI.class) {
            ZogUtils.i("stop voice nlp");

            if (!checkAIUIAgent()) {
                return;
            }
            // 停止录音
            String params = "sample_rate=16000,data_type=audio";
            AIUIMessage stopWriteMsg = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, params, null);

            mAIUIAgent.sendMessage(stopWriteMsg);
        }
    }


    private String getAIUIParams() {
        String params = "";

        AssetManager assetManager = ResWrapper.getInstance().getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }


    private boolean checkAIUIAgent() {

        if (activity == null) {
            ZogUtils.e("activity not set");
        }

        if (mAIUIListener == null) {
            ZogUtils.e("AIUIListener not set");
        }

        if (null == mAIUIAgent) {
            ZogUtils.i("create aiui agent");
            mAIUIAgent = AIUIAgent.createAgent(activity, getAIUIParams(), mAIUIListener);
            AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
            mAIUIAgent.sendMessage(startMsg);
        }

        if (null == mAIUIAgent) {
            final String strErrorTip = "创建 AIUI Agent 失败！";
            ZogUtils.e(strErrorTip);
        }

        return null != mAIUIAgent;
    }


    /**
     * 销毁
     */
    public void destory() {
        if (null != this.mAIUIAgent) {
            AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
            mAIUIAgent.sendMessage(stopMsg);

            this.mAIUIAgent.destroy();
            this.mAIUIAgent = null;
        }
        activity = null;
        mInstance = null;
    }

    public static void init(Application application, String iflytekAppId) {
        synchronized (AIUI.class) {
            SpeechUtility.createUtility(application, "appid=" + iflytekAppId);
        }
    }


    private AIUIListener mAIUIListener;

    private int mAIUIState = AIUIConstant.STATE_IDLE;

    private AIUIAgent mAIUIAgent;

    private Activity activity;

    private static AIUI mInstance;

    private AIUI() {
    }

    public static AIUI with(Activity activity) {
        if (mInstance == null) {
            mInstance = new AIUI();
            mInstance.activity = activity;
        }
        return mInstance;
    }
}
