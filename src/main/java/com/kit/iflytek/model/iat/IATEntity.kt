package com.kit.iflytek.model.iat

/**
 *
 * 语音听写实体类
 *
 *
 * JSON字段	英文全称	类型	说明
 * sn	sentence	number	第几句
 * ls	last sentence	boolean	是否最后一句
 * bg	begin	number	开始
 * ed	end	number	结束
 * ws	words	array	词
 * cw	chinese word	array	中文分词
 * w	word	string	单字
 * sc	score	number	分数
 *
 *
 * Created by Zhao on 2017/12/20.
 */

data class IATEntity(val sn: Int, val ls: Boolean, val bg: Int, val ed: Boolean, val ws: ArrayList<Words>)

data class Words(val bg: Int, val cw: ArrayList<ChineseWord>)


data class ChineseWord(val sc: Int, val w: String)
