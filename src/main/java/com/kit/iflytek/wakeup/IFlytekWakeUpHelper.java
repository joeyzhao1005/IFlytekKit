//package com.kit.iflytek.wakeup;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechEvent;
//import com.iflytek.cloud.SpeechUtility;
//import com.iflytek.cloud.VoiceWakeuper;
//import com.iflytek.cloud.util.ResourceUtil;
//import com.iflytek.cloud.RecognizerResult;
//import com.iflytek.cloud.WakeuperListener;
//import com.iflytek.cloud.WakeuperResult;
//import com.kit.extend.iflytek.R;
//import com.kit.utils.ResWrapper;
//import com.kit.utils.log.ZogUtils;
//
//import org.json.JSONException;
//
///**
// * Created by Zhao on 2017/2/18.
// */
//
//public class IFlytekWakeUpHelper {
//
//    private Context context;
//    private VoiceWakeuper mIvw;
//
//    private int curThresh = 0;
//
//
//    private IFlytekWakeUpCallback callback;
//
//    public IFlytekWakeUpHelper(Context context) {
//        this.context = context;
//
//        //1.加载唤醒词资源，resPath为唤醒资源路径
//        StringBuffer param = new StringBuffer();
//        String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + ResWrapper.getInstance().getString(R.string.iflytek_app_id) + ".jet");
//        param.append(ResourceUtil.IVW_RES_PATH + "=" + resPath);
//        param.append("," + ResourceUtil.ENGINE_START + "=" + SpeechConstant.ENG_IVW);
//        SpeechUtility.getUtility().setParameter(ResourceUtil.ENGINE_START, param.toString());
//        //2.创建VoiceWakeuper对象
//        mIvw = VoiceWakeuper.createWakeuper(context, null);
//        //3.设置唤醒参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
//        //唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
//        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
//        //设置当前业务类型为唤醒
//        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
//        //设置唤醒一直保持，直到调用stopListening，传入0则完成一次唤醒后，会话立即结束（默认0）
//        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
//    }
//
//
//    public IFlytekWakeUpCallback getCallback() {
//        return callback;
//    }
//
//    public void setCallback(IFlytekWakeUpCallback callback) {
//        this.callback = callback;
//    }
//
//    //听写监听器
//    private WakeuperListener mWakeuperListener = new WakeuperListener() {
//        public void onResult(WakeuperResult result) {
//            try {
//                String text = result.getResultString();
//                ZogUtils.i(text);
//
//                if(text.contains("wakeup")&&callback!=null){
//                    callback.onIFlytekWakeUpEventWakeUp();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        @Override
//        public void onVolumeChanged(int i) {
//
//        }
//
//        public void onError(SpeechError error) {
//        }
//
//        public void onBeginOfSpeech() {
//        }
//
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            if (SpeechEvent.EVENT_IVW_RESULT == eventType) {
//                //当使用唤醒+识别功能时获取识别结果
//                //arg1:是否最后一个结果，1:是，0:否。
//                RecognizerResult reslut = ((RecognizerResult) obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT));
//            }
//        }
//    };
//
//    public void start() {
//        //4.开始唤醒
//        mIvw.startListening(mWakeuperListener);
//
//    }
//
//    public void stop() {
//        if (mIvw != null)
//            mIvw.stopListening();
//
//    }
//
//    public void destory() {
//        if (mIvw != null)
//            mIvw.destroy();
//
//    }
//
//
//
//    public interface IFlytekWakeUpCallback {
//
//        public void onIFlytekWakeUpEventWakeUp();
//
//    }
//}
