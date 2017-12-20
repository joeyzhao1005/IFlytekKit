package com.kit.iflytek.model.nlp

import com.kit.iflytek.model.iat.Words

/**
 *
 * 返回结果data如下：
 * 参数	类型	必须	说明
 * rc	int	是	应答码(response code)
 * text	String	是	用户的输入，可能和请求中的原始text不完全一致，因服务器可能会对text进行语言纠错
 * vendor	String	否	技能提供者，不存在时默认表示为IFLYTEK提供的开放技能
 * service	String	是	技能的全局唯一名称，一般为vendor.name，vendor不存在时默认为IFLYTEK提供的开放技能。
 * semantic	Array	否	本次语义（包括历史继承过来的语义）结构化表示，各技能自定义
 * data	Object	否	数据结构化表示，各技能自定义
 * answer	Object	否	对结果内容的最简化文本/图片描述，各技能自定义
 * dialog_stat	String	否	用于客户端判断是否使用信源返回数据
 * moreResults	Object	否	在存在多个候选结果时，用于提供更多的结果描述
 * sid	String	是	本次服务唯一标识
 * Created by Zhao on 2017/12/21.
 */

data class NLPEntity(val rc: Int, val text: String, val vendor: String, val service: String
                     , val semantic: ArrayList<Semantic>, val data: String, val answer: Answer
                     , val dialog_stat: String, val moreResults: String, val sid: String
                     , val man_intv: String, val no_nlu_result: Int, val operation: String
                     , val status: Int, val uuid: String)


/**
 * 语义（包括历史继承过来的语义）结构化表示，各技能自定义 实体类
 *
 * 包含在NLPEntity 中的
 */
data class Semantic(val rc: Int, val text: String, val vendor: String, val service: String, val semantic: ArrayList<Words>)


/**
 * 答案 实体类
 *
 * 包含在NLPEntity 中的
 */
data class Answer(val text: String, val answerType: String, val emotion: String, val question: Question, val topicID: String, val type: String)


/**
 * 问题 实体类
 *
 * 包含在Answer 中的
 */
data class Question(val question: String, val question_ws: String)

