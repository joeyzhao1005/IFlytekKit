package com.kit.iflytek.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.DataDownloader;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.UserWords;
import com.kit.app.enums.CharsetName;
import com.kit.config.AppConfig;
import com.kit.extend.iflytek.R;
import com.kit.iflytek.speech.setting.IatSettings;
import com.kit.iflytek.speech.setting.TtsSettings;
import com.kit.iflytek.speech.setting.UnderstanderSettings;
import com.kit.iflytek.speech.util.FucUtil;
import com.kit.utils.AppUtils;
import com.kit.utils.ResWrapper;
import com.kit.utils.ToastUtils;
import com.kit.utils.log.ZogUtils;

import java.io.UnsupportedEncodingException;


public class IFlytekTools {


    // 引擎类型
    public static String mEngineType = SpeechConstant.TYPE_CLOUD;


    private SharedPreferences mTtsSettingsSharedPreferences;
    protected SharedPreferences mIatSettingsSharedPreferences;

    private SharedPreferences mUnderstanderSettingsSharedPreferences;

    private SpeechSynthesizer mTts;

    private SpeechUnderstander mSpeechUnderstander;

    private TextUnderstander mTextUnderstander;

    /**
     * 语音听写对象
     */
    protected SpeechRecognizer asr;


//    public static UnderstandResponse getCommondUnderstandResponse(String commondStr) {
//        UnderstandResponse ur = new UnderstandResponse();
//        ur.setText( commondStr;
//
//        UnderstandResponse understandResponse = WithUTools.getCommandUnderstandResponse(ur);
//        if (understandResponse != null)
//            ur.setAnswer(understandResponse.answer;
//
//
////        ur = DoUnderstandResponse.getLocalUnderstandResponse(commondStr, interceptCommandAnswer.text);
//
//        return ur;
//    }

    private static IFlytekTools iFlytekTools;

    public static IFlytekTools getInstance() {
        if (iFlytekTools == null) {
            iFlytekTools = new IFlytekTools();
        }

        return iFlytekTools;
    }

    public IFlytekTools() {
        final Context context = ResWrapper.getInstance().getApplicationContext();

        mUnderstanderSettingsSharedPreferences = context.getSharedPreferences(
                UnderstanderSettings.PREFER_NAME, Context.MODE_PRIVATE);

        mTtsSettingsSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME
                , Context.MODE_PRIVATE);
        mIatSettingsSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME
                , Activity.MODE_PRIVATE);


        // 初始化文字转语音
        initTTS(context);

        // 初始化语音语意理解
        initSpeechUnderstander(context);

        // 初始化文字语意理解
        initTextUnderstander(context);

        // 初始化识别对象(听写)
        initListener(context);

        //初始化联系人名字和关键字
        initUserWordsListener();

        //初始化语音合成人
        initVoicer(context, -1);


//        new Thread() {
//            public void run() {
//                uploadContact(context);
//            }
//        }.start();

//        new Thread() {
//            public void run() {
//                uploadUserWords(context, asr, lexiconListener);
//            }
//        }.start();
    }

    public SpeechRecognizer getIat() {
        return asr;
    }

    public SpeechSynthesizer getTts() {
        return mTts;
    }

    public SpeechUnderstander getSpeechUnderstander() {
        return mSpeechUnderstander;
    }


    public TextUnderstander getTextUnderstander() {
        return mTextUnderstander;
    }


    public void downloadUserWords(final Context context) {

        /**
         * 用户词表下载监听器。
         */
        SpeechListener downloadlistener = new SpeechListener() {
            String mDownloadResult = "";
            String mResultText = "";

            @Override
            public void onBufferReceived(byte[] bytes) {
                try {
                    if (bytes != null && bytes.length > 1)
                        mDownloadResult = new String(bytes, CharsetName.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onCompleted(SpeechError error) {
                if (error != null) {
                    ZogUtils.i(error.toString());
                } else if (TextUtils.isEmpty(mDownloadResult)) {
                    ZogUtils.i(context.getString(R.string.text_userword_empty));
                } else {

                    UserWords userwords = new UserWords(mDownloadResult.toString());
                    if (userwords == null || userwords.getKeys() == null) {
                        ZogUtils.i(context.getString(R.string.text_userword_empty));
                        return;
                    }
                    for (String key : userwords.getKeys()) {
                        mResultText += (key + ":");
                        for (String word : userwords.getWords(key)) {
                            mResultText += (word + ",");
                        }
                        mResultText += ("\n");
                    }
                    ZogUtils.i(context.getString(R.string.text_download_success));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }

        };

        DataDownloader dataDownloader = new DataDownloader(context);
        dataDownloader.setParameter(SpeechConstant.SUBJECT, "spp");
        dataDownloader.setParameter(SpeechConstant.DATA_TYPE, "userword");
        dataDownloader.downloadData(downloadlistener);
    }

    public void uploadUserWords(Context context, SpeechRecognizer mIat, LexiconListener lexiconListener) {
        String contents = FucUtil.readFile(context, "userwords", CharsetName.UTF_8);
        //指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, CharsetName.UTF_8);
        int ret = mIat.updateLexicon("userword", contents, lexiconListener);
        if (ret != ErrorCode.SUCCESS)
            ZogUtils.i("上传热词失败,错误码：" + ret);
    }

    /**
     * 上传联系人，以精准的语音识别联系人
     *
     * @param context
     */
    public void uploadContact(Context context) {
        /**
         * 获取联系人监听器。
         */
        ContactManager.ContactListener mContactListener = new ContactManager.ContactListener() {
            @Override
            public void onContactQueryFinish(String contactInfos,
                                             boolean changeFlag) {
                // 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
                if (changeFlag) {
                    // 指定引擎类型
                    asr.setParameter(SpeechConstant.ENGINE_TYPE,
                            SpeechConstant.TYPE_CLOUD);
                    asr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");

                    LexiconListener lexiconListener = new LexiconListener() {

                        @Override
                        public void onLexiconUpdated(String lexiconId, SpeechError error) {
//                            if (error != null) {
//                                ZogUtils.i(error.toString());
//                            } else {
//                                ZogUtils.i(
//                                        context.getString(R.string.text_upload_success));
//                            }
                        }
                    };

                    int ret = asr.updateLexicon("contact", contactInfos,
                            lexiconListener);
//                    if (ret != ErrorCode.SUCCESS)
//                        ZogUtils.e("上传联系人失败：" + ret);
                }
            }

        };

        if (AppUtils.isPermission(Manifest.permission.READ_CONTACTS)) {
            ContactManager mgr = ContactManager.createManager(context,
                    mContactListener);
            mgr.asyncQueryAllContactsName();
        }
    }

    /**
     * @param text                文本提示
     * @param synthesizerListener
     */

    public void doSpeak(String text, SynthesizerListener synthesizerListener) {
        mTts.startSpeaking(text, synthesizerListener);
    }


    public void stopSpeaking() {
        mTts.stopSpeaking();
    }


    public void initVoicer(Context context, int witch) {


        String voicer;


        // 云端发音人名称列表
        String[] cloudVoicersEntries = context.getResources().getStringArray(R.array.voicer_cloud_entries);

        String[] cloudVoicersValue = context.getResources().getStringArray(R.array.voicer_cloud_values);

        try {
            voicer = cloudVoicersValue[witch];
        } catch (Exception e) {
            voicer = mTtsSettingsSharedPreferences.getString("voicer_preference", "vixq");
            // LogUtils.showException(e);
        }

        setTTSParam(voicer);

    }


    public void initUserWordsListener() {
        final Context context = ResWrapper.getInstance().getApplicationContext();


    }

    public void initTTS(Context context) {

        /**
         * 初期化监听。
         */
        InitListener mTtsInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                ZogUtils.i("InitListener tts init() code = "
                        + code);
                // if (code == ErrorCode.SUCCESS) {
                // ((Button) findViewById(R.id.tts_play)).setEnabled(true);
                // }
            }
        };

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
    }

    /**
     * @param
     * @return void 返回类型
     * @Title initListener
     * @Description 初始化识别对象
     */
    public void initListener(Context context) {
        /**
         * 初始化监听器。
         */
        InitListener mInitListener = new InitListener() {

            @Override
            public void onInit(int code) {

                if (code == ErrorCode.SUCCESS) {
                    ZogUtils.i(
                            "SpeechRecognizer init() code = " + code);
                    // findViewById(R.id.iat_recognize).setEnabled(true);
                }
            }
        };

        asr = SpeechRecognizer.createRecognizer(context, mInitListener);
        asr.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    public void initTextUnderstander(Context context) {

        InitListener textUnderstanderListener = new InitListener() {

            @Override
            public void onInit(int code) {
                ZogUtils.i(
                        "textUnderstanderListener init() code = " + code);
//                 if (code == ErrorCode.SUCCESS) {
                // findViewById(R.id.text_understander).setEnabled(true);
                // }
            }
        };

        // 初始化语意理解
        mTextUnderstander = TextUnderstander.createTextUnderstander(context,
                textUnderstanderListener);
    }

    /**
     * 初始化语意理解器
     *
     * @param context
     */
    public void initSpeechUnderstander(Context context) {

        InitListener speechUnderstanderListener = new InitListener() {
            @Override
            public void onInit(int code) {
                ZogUtils.i(
                        "speechUnderstanderListener init() code = " + code);
                // if (code == ErrorCode.SUCCESS) {
                // findViewById(R.id.start_understander).setEnabled(true);
                // }
            }
        };
        // 初始化语意理解
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(context,
                speechUnderstanderListener);

        // mSpeechUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
    }


    public void listen2Text(Context context,
                            RecognizerListener recognizerListener) {
        setAsrParam(context);
        // 不显示听写对话框
        int ret = asr.startListening(recognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            ZogUtils.i("听写失败,错误码：" + ret);
        } else {
            ZogUtils.i(context.getString(R.string.text_begin));
        }
    }

    public void understandSpeech(Context context,
                                 SpeechUnderstanderListener mRecognizerListener) {
        setUnderstandParam();
        if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
            mSpeechUnderstander.stopUnderstanding();
//            ToastUtils.mkToast("停止录音", 1500);
        } else {
            int ret = mSpeechUnderstander
                    .startUnderstanding(mRecognizerListener);
            if (ret != 0) {
                ToastUtils.mkToast("语义理解失败,错误码:" + ret, 1500);
            }
        }
    }

//    public static TextUnderstanderListener textUnderstanderListener = new TextUnderstanderListener() {
//
//        @Override
//        public void onResult(final UnderstanderResult result) {
//
//            if (null != result) {
//
//                String text = result.getResultString();
//                // 显示
//                LogUtils.i(getClass(), "understander result：" + text);
//
//                // if (!TextUtils.isEmpty(text)) {
//                // mUnderstanderText.setText(text);
//                // }
//            } else {
//                LogUtils.i(getClass(), "understander result:null");
//                // ToastUtils.mkToast("识别结果不正确。");
//            }
//
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            LogUtils.i(getClass(),
//                    "onError Code：" + error.getErrorCode());
//
//        }
//    };


    /**
     * 参数设置
     *
     * @return
     */
    @SuppressLint("SdCardPath")
    public void setAsrParam(Context context) {


        String lag = mIatSettingsSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            asr.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            asr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            asr.setParameter(SpeechConstant.ACCENT, lag);
        }

        asr.setParameter(SpeechConstant.ENGINE_MODE, null);

        // 设置语音前端点
        asr.setParameter(SpeechConstant.VAD_BOS, mIatSettingsSharedPreferences.getString("iat_vadbos_preference", "4000"));
        // 设置语音后端点
        asr.setParameter(SpeechConstant.VAD_EOS, mIatSettingsSharedPreferences.getString("iat_vadeos_preference", "1000"));
        // 设置标点符号
        asr.setParameter(SpeechConstant.ASR_PTT, mIatSettingsSharedPreferences.getString("iat_punc_preference", "1"));
        // 设置音频保存路径
        asr.setParameter(SpeechConstant.ASR_AUDIO_PATH, AppConfig.getAppConfig().getCacheDir() + "iflytek/wavaudio.pcm");
    }


    /**
     * 参数设置
     *
     * @return
     */
    @SuppressLint("SdCardPath")
    public void setUnderstandParam() {
        // 支持普通话
        String lag = mUnderstanderSettingsSharedPreferences.getString(
                "understander_language_preference", "mandarin");

        if (lag.equals("en_us")) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,
                mUnderstanderSettingsSharedPreferences.getString("understander_vadbos_preference",
                        "4000"));
        // 设置语音后端点
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,
                mUnderstanderSettingsSharedPreferences.getString("understander_vadeos_preference",
                        "1000"));
        // 设置标点符号
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT,
                mUnderstanderSettingsSharedPreferences.getString("understander_punc_preference",
                        "1"));
        // 设置音频保存路径
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                AppConfig.getAppConfig().getCacheDir() + "iflytek/wavaudio.pcm");


//        SharedPreferences.Editor mEditor =  mUnderstanderSettingsSharedPreferences.edit();
//        mEditor .putString("understander_language_preference", "mandarin");
//        mEditor.commit();


    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setTTSParam(String voicer) {

        //设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        }


        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

        //设置语速
        mTts.setParameter(SpeechConstant.SPEED, mTtsSettingsSharedPreferences.getString("speed_preference", "50"));

        //设置音调
        mTts.setParameter(SpeechConstant.PITCH, mTtsSettingsSharedPreferences.getString("pitch_preference", "50"));

        //设置音量
        mTts.setParameter(SpeechConstant.VOLUME, mTtsSettingsSharedPreferences.getString("volume_preference", "50"));

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mTtsSettingsSharedPreferences.getString("stream_preference", "3"));

        //LogUtils.i(class, "setTTSParam:" + voicer);

        //记住选择

        SharedPreferences.Editor mEditor = mTtsSettingsSharedPreferences.edit();
        mEditor.putString("voicer_preference", voicer);
        mEditor.commit();
    }


}
