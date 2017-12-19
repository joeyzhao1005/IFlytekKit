package com.kit.iflytek.mangaer

import android.app.Activity
import android.app.Application
import android.content.Context
import com.iflytek.aiui.AIUIAgent
import com.iflytek.aiui.AIUIConstant
import com.iflytek.aiui.AIUIListener
import com.iflytek.aiui.AIUIMessage
import com.iflytek.cloud.SpeechUtility
import com.kit.utils.ResWrapper
import com.kit.utils.log.ZogUtils
import java.io.IOException


class AIUI private constructor() {


    @Synchronized
    fun listen() {
        val wakeupMsg = AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null)
        mAIUIAgent?.sendMessage(wakeupMsg)
    }


    /**
     * 开始语音理解
     */
    @Synchronized
    fun startVoiceNlp(context: Context): AIUI {

        ZogUtils.i("start voice nlp")

        if (!checkAIUIAgent(context)) {
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
    fun AIUIListener(aiuiListener: AIUIListener): AIUI {
        this.mAIUIListener = aiuiListener
        return this
    }


    /**
     * 停止语音理解
     */
    @Synchronized
    fun stopVoiceNlp(context: Context) {

        ZogUtils.i("stop voice nlp")

        if (!checkAIUIAgent(context)) {
            return
        }
        // 停止录音
        val params = "sample_rate=16000,data_type=audio"
        val stopWriteMsg = AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, params, null)

        mAIUIAgent?.sendMessage(stopWriteMsg)
    }


    private fun checkAIUIAgent(context: Context): Boolean {

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


    companion object {
        fun init(application: Application, iflytekAppId: String) {
            SpeechUtility.createUtility(application, "appid=" + iflytekAppId)
        }

        fun get(): AIUI? {
            return INSTANCE
        }

        val INSTANCE: AIUI by lazy { AIUI() }
    }
}
