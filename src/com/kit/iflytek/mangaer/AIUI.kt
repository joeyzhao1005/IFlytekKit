package com.kit.iflytek.mangaer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.iflytek.aiui.AIUIAgent
import com.iflytek.aiui.AIUIConstant
import com.iflytek.aiui.AIUIListener
import com.iflytek.aiui.AIUIMessage
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ContactManager
import com.kit.iflytek.speech.util.FucUtil
import com.kit.utils.ResWrapper
import com.kit.utils.log.ZogUtils
import org.json.JSONObject
import java.io.IOException

class AIUI private constructor() {


    /**
     * 听写组件
     */
    inner class IAT internal constructor() {


        /**
         * 开始听写
         */
        @Synchronized
        fun listen() {
            val ret = mIat?.startListening(mRecognizerListener)
            if (ret != ErrorCode.SUCCESS) {
                ZogUtils.e("听写失败,错误码：" + ret)
            } else {
                ZogUtils.i("我在听，开始说话吧")
            }
        }

        /**
         * 停止听写
         */
        @Synchronized
        fun iatStop() {
            mIat?.stopListening()
        }


        /**
         * 取消听写
         */
        @Synchronized
        fun iatCancel() {
            mIat?.cancel()
        }

        /**
         * 上传联系人 便于识别
         */
        @Synchronized
        fun iatUploadContact(context: Context, mContactListener: ContactManager.ContactListener) {
            val mgr = ContactManager.createManager(context,
                    mContactListener)
            mgr.asyncQueryAllContactsName()
        }

        /**
         * 上传用户词表 关键词
         */
        @Synchronized
        fun uploadUserwords(context: Context, mLexiconListener: LexiconListener) {
            ZogUtils.i("上传用户词表")
            val contents = FucUtil.readFile(context, "userwords", "utf-8")
            ZogUtils.i(contents)
            // 指定引擎类型
            mIat?.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
            mIat?.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8")
            val ret = mIat?.updateLexicon("userword", contents, mLexiconListener)
            if (ret != ErrorCode.SUCCESS)
                ZogUtils.e("上传热词失败,错误码：" + ret)
        }


        /**
         * 设置监听
         */
        @Synchronized
        fun iatListener(recognizerListener: RecognizerListener): IAT {
            this.mRecognizerListener = recognizerListener
            return this
        }

        fun create(): IAT {
            setParam()
            return this
        }

        @Synchronized
        private fun setParam() {
            setParam(false, "mandarin")
        }

        /**
         * 参数设置
         *
         * @return
         */
        fun setParam(translateEnable: Boolean, lag: String) {
            // 清空参数
            mIat?.setParameter(SpeechConstant.PARAMS, null)

            // 设置听写引擎
            mIat?.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType)
            // 设置返回结果格式
            mIat?.setParameter(SpeechConstant.RESULT_TYPE, "json")

            this.mTranslateEnable = translateEnable
            if (mTranslateEnable) {
                ZogUtils.i("translate enable")
                mIat?.setParameter(SpeechConstant.ASR_SCH, "1")
                mIat?.setParameter(SpeechConstant.ADD_CAP, "translate")
                mIat?.setParameter(SpeechConstant.TRS_SRC, "its")
            }

            laungue = laungue
            if (lag == "en_us") {
                // 设置语言
                mIat?.setParameter(SpeechConstant.LANGUAGE, "en_us")
                mIat?.setParameter(SpeechConstant.ACCENT, null)

                if (mTranslateEnable) {
                    mIat?.setParameter(SpeechConstant.ORI_LANG, "en")
                    mIat?.setParameter(SpeechConstant.TRANS_LANG, "cn")
                }
            } else {
                // 设置语言
                mIat?.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
                // 设置语言区域
                mIat?.setParameter(SpeechConstant.ACCENT, lag)

                if (mTranslateEnable) {
                    mIat?.setParameter(SpeechConstant.ORI_LANG, "cn")
                    mIat?.setParameter(SpeechConstant.TRANS_LANG, "en")
                }
            }

            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
            mIat?.setParameter(SpeechConstant.VAD_BOS, vadBOS)

            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
            mIat?.setParameter(SpeechConstant.VAD_EOS, vadEOS)

            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIat?.setParameter(SpeechConstant.ASR_PTT, asrPTT)

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            mIat?.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
            mIat?.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory().toString() + "/msc/iat.wav")
        }

        /**
         * 销毁
         */
        @Synchronized
        fun destory() {
            if (null != mIat) {
                // 退出时释放连接
                mIat?.cancel()
                mIat?.destroy()
            }
            mIat = null;
        }

        /**
         * 听写监听器。
         */
        private var mRecognizerListener: RecognizerListener? = null


        fun isTranslateEnable(): Boolean {
            return mTranslateEnable
        }


        private val vadBOS = "4000" //前端点

        private val vadEOS = "1000" //后端点
        private val asrPTT = "1"    //是否显示标点符号


        private var laungue = "mandarin"


        private var mTranslateEnable = false

        private var mEngineType = SpeechConstant.TYPE_CLOUD

        private var mIat: SpeechRecognizer? = null


    }

    /**
     * 语音理解
     */
    inner class NLP internal constructor() {

        @Synchronized
        fun wakeUp() {
            val wakeupMsg = AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null)
            mAIUIAgent?.sendMessage(wakeupMsg)
        }

        /**
         * 开始语音理解
         */
        @Synchronized
        fun nlpStartVoice(context: Context): NLP {

            ZogUtils.i("start voice nlp")

            if (!nlpCheckAIUIAgent(context)) {
                return this
            }

            // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
            // 默认为oneshot 模式，即一次唤醒后就进入休眠，如果语音唤醒后，需要进行文本语义，请将改段逻辑copy至startTextNlp()开头处
            if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
                val wakeupMsg = AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null)
                mAIUIAgent?.sendMessage(wakeupMsg)
            }

            // 打开AIUI内部录音机，开始录音
            val params = "sample_rate=16000,data_type=audio"
            val writeMsg = AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null)
            mAIUIAgent?.sendMessage(writeMsg)
            return this
        }


        /**
         * 停止语音理解
         */
        @Synchronized
        fun nlpStopVoice(context: Context) {

            ZogUtils.i("stop voice nlp")

            if (!nlpCheckAIUIAgent(context)) {
                return
            }
            // 停止录音
            val params = "sample_rate=16000,data_type=audio"
            val stopWriteMsg = AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, params, null)

            mAIUIAgent?.sendMessage(stopWriteMsg)
        }


        /**
         * 设置监听
         */
        fun nlpListener(aiuiListener: AIUIListener): NLP {
            this.mAIUIListener = aiuiListener
            return this
        }


        /**
         * 更新词库
         */
        fun nlpUpdateLexicon(context: Context) {
            var params: String? = null
            val contents = FucUtil.readFile(context, "userwords", "utf-8")
            try {
                val joAiuiLexicon = JSONObject()
                joAiuiLexicon.put("name", "userword")
                joAiuiLexicon.put("content", contents)
                params = joAiuiLexicon.toString()
            } catch (e: Throwable) {
                e.printStackTrace()
                ZogUtils.e(e.localizedMessage)
            }
            //end of try-catch

            ZogUtils.i(contents)

            val msg = AIUIMessage(AIUIConstant.CMD_UPLOAD_LEXICON, 0, 0, params, null)
            mAIUIAgent?.sendMessage(msg)
        }


        /**
         * aiui 语音理解检查
         *
         * aiui 的语音理解是开始之后一直不停止，可以连续的监听
         * 但是一旦听到之后 就必须重新发送CMD_START
         */
        private fun nlpCheckAIUIAgent(context: Context): Boolean {

            if (context == null) {
                ZogUtils.e("activity not set")
            }

            if (mAIUIListener == null) {
                ZogUtils.e("AIUIListener not set")
            }

            if (null == mAIUIAgent) {
                ZogUtils.i("create aiui agent")
                mAIUIAgent = AIUIAgent.createAgent(context, aiuiParams, mAIUIListener)
                val startMsg = AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null)
                mAIUIAgent!!.sendMessage(startMsg)
            }

            if (null == mAIUIAgent) {
                val strErrorTip = "创建 AIUI Agent 失败！"
                ZogUtils.e(strErrorTip)
            }

            return null != mAIUIAgent
        }


        private val aiuiParams: String
            get() {
                var params = ""

                val assetManager = ResWrapper.getInstance().resources.assets
                try {
                    val ins = assetManager.open("cfg/aiui_phone.cfg")
                    val buffer = ByteArray(ins.available())

                    ins.read(buffer)
                    ins.close()
//                params = buffer.toString()
                    params = String(buffer)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return params
            }

        private var mAIUIListener: AIUIListener? = null

        private val mAIUIState = AIUIConstant.STATE_IDLE

        private var mAIUIAgent: AIUIAgent? = null


        /**
         * 销毁
         */
        @Synchronized
        fun destory() {
            if (null != this.mAIUIAgent) {
                val stopMsg = AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null)
                mAIUIAgent?.sendMessage(stopMsg)

                try {
                    this.mAIUIAgent?.destroy()
                } catch (e: Exception) {
                }
            }
            this.mAIUIAgent = null
        }

    }

    /**
     * 销毁
     */
    @Synchronized
    fun destory() {
        NLP_INSTANCE.destory()
    }


    companion object {
        fun init(application: Application, iflytekAppId: String) {
            SpeechUtility.createUtility(application, "appid=" + iflytekAppId)
        }

        fun get(): AIUI? {
            return INSTANCE
        }


        val IAT_INSTANCE: IAT by lazy { INSTANCE.IAT() }
        val NLP_INSTANCE: NLP by lazy { INSTANCE.NLP() }

        val INSTANCE: AIUI by lazy { AIUI() }
    }
}
