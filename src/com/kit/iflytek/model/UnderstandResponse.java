package com.kit.iflytek.model;

import com.kit.utils.GsonUtils;

public class UnderstandResponse {
	/**
	 * @IsMust true
	 * 
	 * @Describe 应答码(response code)
	 */
	public int rc;

	/**
	 * @IsMust false
	 * 
	 * @Describe 错误信息
	 */
	public Error error;

	/**
	 * @IsMust true
	 * 
	 * @Describe 用户的输入,可能和请求中的原始 text不完全一致,因服务器可能会 对text进行语言纠错
	 */
	public String text;

	/**
	 * @IsMust false
	 * 
	 * @Describe 上下文信息,客户端需要将该字段 结果传给下一次请求的history字段
	 */
	public String history;

	/**
	 * @IsMust true
	 * 
	 * @Describe 服务的全局唯一名称,详情参见4.通用服务
	 */
	public String service;

	/**
	 * @IsMust true
	 * 
	 * @Describe 服务的细分操作编码,各业务服务 自定义
	 */
	public String operation;

	/**
	 * @IsMust false
	 * 
	 * @Describe 语义结构化表示,各服务自定义
	 */
	public Semantic semantic;

	/**
	 * @IsMust false
	 * 
	 * @Describe 数据结构化表示,各服务自定义
	 */
	public Data data;

	/**
	 * @IsMust false
	 * 
	 * @Describe 该字段提供了结果内容的HTML5 页面,客户端可以无需解析处理直 接展现
	 */
	public WebPage webPage;

	/**
	 * @IsMust false
	 * 
	 * @Describe 对结果内容的最简化文本/图片描述,各服务自定义
	 */
	public Answer answer;

	/**
	 * @IsMust false
	 * 
	 * @Describe 结果内容的关联信息,作为用户后续交互的引导展现
	 */
	public Tips tips;

	/**
	 * @IsMust false
	 * 
	 * @Describe 在存在多个候选结果时,用于提供更多的结果描述
	 */
	public UnderstandResponse[] moreResults;


	@Override
	public UnderstandResponse clone(){
		return GsonUtils.getObj(GsonUtils.toJson(this),UnderstandResponse.class);
	}

}
