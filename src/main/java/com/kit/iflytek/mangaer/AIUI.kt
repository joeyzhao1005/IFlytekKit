package com.kit.iflytek.mangaer

import android.app.Application
import android.content.Context
import android.os.Environment
import com.iflytek.aiui.AIUIAgent
import com.iflytek.aiui.AIUIConstant
import com.iflytek.aiui.AIUIListener
import com.iflytek.aiui.AIUIMessage
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ContactManager
import com.kit.iflytek.speech.util.FucUtil
import com.kit.utils.ResWrapper
import com.kit.utils.log.Zog
import org.json.JSONObject
import java.io.IOException

class AIUI private constructor() {


    /**
     * 文本语意理解
     */
    inner class TXTUnderstander internal constructor() {
        /**
         * 开始听写
         */
        @Synchronized
        fun understand(text: String) {
            if (mTextUnderstander.isUnderstanding) {
                mTextUnderstander?.cancel()
                Zog.i("取消")
            } else {
                // 设置语义情景
                //mTextUnderstander.setParameter(SpeechConstant.SCENE, "main");
                val ret = mTextUnderstander.understandText(text, textUnderstanderListener)
                if (ret != 0) {
                    Zog.e("语义理解失败,错误码:" + ret)
                }
            }
        }


        /**
         * 取消
         */
        @Synchronized
        fun understandCancel() {
            mTextUnderstander?.cancel()
        }


        /**
         * 设置监听
         */
        @Synchronized
        fun textUnderstanderListener(textUnderstanderListener: TextUnderstanderListener): TXTUnderstander {
            this.textUnderstanderListener = textUnderstanderListener
            return this
        }

        fun createTextUnderstander(context: Context): TXTUnderstander {
            mTextUnderstander = TextUnderstander.createTextUnderstander(context, mTextUdrInitListener)
            return this
        }

        /**
         * 初始化监听器（文本到语义）。
         */
        private val mTextUdrInitListener = InitListener { code ->
            Zog.d("textUnderstanderListener init() code = " + code)
            if (code != ErrorCode.SUCCESS) {
                Zog.i("初始化失败,错误码：" + code)
            }
        }

        /**
         * 销毁
         */
        @Synchronized
        fun destory() {
            if (null != mTextUnderstander) {
                if (mTextUnderstander.isUnderstanding)
                    mTextUnderstander?.cancel()

                mTextUnderstander?.destroy()
            }
        }


        private var textUnderstanderListener: TextUnderstanderListener? = null
        private lateinit var mTextUnderstander: TextUnderstander

    }


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
                Zog.e("听写失败,错误码：" + ret)
            } else {
                Zog.i("我在听，开始说话吧")
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
            Zog.i("上传用户词表")
            val contents = FucUtil.readFile(context, "userwords", "utf-8")
            Zog.i(contents)
            // 指定引擎类型
            mIat?.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
            mIat?.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8")
            val ret = mIat?.updateLexicon("userword", contents, mLexiconListener)
            if (ret != ErrorCode.SUCCESS)
                Zog.e("上传热词失败,错误码：" + ret)
        }


        /**
         * 设置监听
         */
        @Synchronized
        fun iatListener(recognizerListener: RecognizerListener): IAT {
            this.mRecognizerListener = recognizerListener
            return this
        }

        fun create(context: Context, isTranslateEnable: Boolean?, laungue: String?): IAT {
            mIat = SpeechRecognizer.createRecognizer(context, mInitListener)

            setParam(isTranslateEnable, laungue)
            return this
        }


        /**
         * 参数设置
         *
         * @return
         */
        private fun setParam(translateEnable: Boolean?, lag: String?) {
            this.mTranslateEnable = translateEnable ?: false

            this.laungue = lag ?: "mandarin"

            // 清空参数
            mIat?.setParameter(SpeechConstant.PARAMS, null)

            // 设置听写引擎
            mIat?.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType)
            // 设置返回结果格式
            mIat?.setParameter(SpeechConstant.RESULT_TYPE, "json")

            if (mTranslateEnable) {
                Zog.i("translate enable")
                mIat?.setParameter(SpeechConstant.ASR_SCH, "1")
                mIat?.setParameter(SpeechConstant.ADD_CAP, "translate")
                mIat?.setParameter(SpeechConstant.TRS_SRC, "its")
            }

            if (laungue == "en_us") {
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
                mIat?.setParameter(SpeechConstant.ACCENT, laungue)

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
            mIat = null
        }

        /**
         * 初始化监听器。
         */
        private val mInitListener = InitListener { code ->
            Zog.d("SpeechRecognizer init() code = " + code)
            if (code != ErrorCode.SUCCESS) {
                Zog.i("初始化失败，错误码：" + code)
            }
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

            Zog.i("start voice nlp")

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

            Zog.i("stop voice nlp")

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
                Zog.e(e.localizedMessage)
            }
            //end of try-catch

            Zog.i(contents)

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
                Zog.e("activity not set")
            }

            if (mAIUIListener == null) {
                Zog.e("AIUIListener not set")
            }

            if (null == mAIUIAgent) {
                Zog.i("create aiui agent")
                mAIUIAgent = AIUIAgent.createAgent(context, aiuiParams, mAIUIListener)
                val startMsg = AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null)
                mAIUIAgent!!.sendMessage(startMsg)
            }

            if (null == mAIUIAgent) {
                val strErrorTip = "创建 AIUI Agent 失败！"
                Zog.e(strErrorTip)
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
        IAT_INSTANCE.destory()
    }


    companion object {
        fun init(application: Application, iflytekAppId: String) {
            SpeechUtility.createUtility(application, "appid=" + iflytekAppId)
        }

        fun get(): AIUI? {
            return INSTANCE
        }

        val TXT_UNDERSTANDER_INSTANCE: TXTUnderstander by lazy { INSTANCE.TXTUnderstander() }//文本理解

        val IAT_INSTANCE: IAT by lazy { INSTANCE.IAT() }//听写
        val NLP_INSTANCE: NLP by lazy { INSTANCE.NLP() }//语音理解

        val INSTANCE: AIUI by lazy { AIUI() }
    }
}
