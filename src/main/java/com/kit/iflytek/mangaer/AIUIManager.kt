package com.kit.iflytek.mangaer

import com.kit.iflytek.model.iat.IATEntity
import com.kit.iflytek.model.nlp.NLPEntity
import com.kit.utils.GsonUtils
import com.kit.utils.RandomUtils
import com.kit.utils.StringUtils

/**
 * Created by Zhao on 2017/12/21.
 */

object AIUIManager {


    /**
     * 从 IAT 语音听写内容中拼接出语句
     */
    fun textFromAnswer(resultStr: String): String {
        val nlpEntity = GsonUtils.getObj(resultStr, NLPEntity::class.java)

        if (nlpEntity == null
                || nlpEntity.rc == 4
                || StringUtils.isEmptyOrNullStr(nlpEntity.text)
                || StringUtils.isEmptyOrNullStr(nlpEntity.answer?.text)) {
            return defaultAnswer()
        } else {
            return if (StringUtils.isEmptyOrNullStr(nlpEntity.answer.text)) defaultAnswer() else nlpEntity.answer.text
        }
    }


    /**
     * 从 IAT 语音听写内容中拼接出语句
     */
    fun textFromIATEntity(resultStr: String): String {

        val iatEntity = GsonUtils.getObj(resultStr, IATEntity::class.java)

        var userSayDisplay = ""

        for (word in iatEntity?.ws.orEmpty()) {
            for (wd in word.cw) {
                userSayDisplay = userSayDisplay.plus(wd.w ?: "")
            }
        }
        return userSayDisplay
    }


    fun defaultAnswer(): String {
        val default = listOf<String>("我是笨笨，最聪明的笨笨~", "我没能理解你在说什么...")
        val random = RandomUtils.getRandomIntNum(0, default.size - 1)

        return default[random]
    }
}