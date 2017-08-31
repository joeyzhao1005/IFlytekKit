package com.kit.iflytek.model;

import com.google.gson.reflect.TypeToken;
import com.kit.utils.ArrayUtils;
import com.kit.utils.GsonUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.StringUtils;

import java.util.ArrayList;

public class UnderstandResponse {

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Tips getTips() {
        return tips;
    }

    public void setTips(Tips tips) {
        this.tips = tips;
    }

    public UnderstandResponse[] getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(UnderstandResponse[] moreResults) {
        this.moreResults = moreResults;
    }

    public Semantic getSemanticOne() {
        if (semanticArrayList == null) {
            getSemantic();
        }

        if (ListUtils.isNullOrEmpty(semanticArrayList)) {
            return null;
        }
        return semanticArrayList.get(0);
    }

    private ArrayList<Semantic> getSemantic() {

        if (semantic==null)
            return null;
        String ss = GsonUtils.toJson(semantic);
        if (ss.startsWith("[")) {

//    ArrayList<Semantic> appIconInfos = GsonUtils.getArrayList(semantic, new TypeToken<ArrayList<Semantic>>() {
//    }.getType());

            semanticArrayList = GsonUtils.getArrayList(ss);
        } else if (ss.startsWith("{")) {
            Semantic s = GsonUtils.getObj(ss, Semantic.class);
            semanticArrayList = new ArrayList<>();
            semanticArrayList.add(s);
        }

        return semanticArrayList;
    }

    ArrayList<Semantic> semanticArrayList = null;

    public void setSemanticOne(Semantic semanticOne) {
        if (semanticOne == null)
            return;
        ArrayList<Semantic> semanticArrayList = new ArrayList<>();

        semanticArrayList.add(semanticOne);
    }


    @Override
    public UnderstandResponse clone() {
        UnderstandResponse understandResponse = new UnderstandResponse();
        understandResponse.setService(this.service);
        understandResponse.setOperation(this.operation);
        understandResponse.setText(this.text);
        understandResponse.setData(this.data);
        understandResponse.setAnswer(this.answer);
        understandResponse.semantic = this.semantic;
        understandResponse.setRc(this.rc);
        return understandResponse;
    }


    /**
     * @IsMust true
     * @Describe 应答码(response code)
     */
    private String rc;

    /**
     * @IsMust false
     * @Describe 错误信息
     */
    private Error error;

    /**
     * @IsMust true
     * @Describe 用户的输入, 可能和请求中的原始 text不完全一致,因服务器可能会 对text进行语言纠错
     */
    private String text;

    /**
     * @IsMust false
     * @Describe 上下文信息, 客户端需要将该字段 结果传给下一次请求的history字段
     */
    private String history;

    /**
     * @IsMust true
     * @Describe 服务的全局唯一名称, 详情参见4.通用服务
     */
    private String service;

    /**
     * @IsMust true
     * @Describe 服务的细分操作编码, 各业务服务 自定义
     */
    private String operation;

    /**
     * @IsMust false
     * @Describe 语义结构化表示, 各服务自定义
     */
    private Object semantic;

    /**
     * @IsMust false
     * @Describe 数据结构化表示, 各服务自定义
     */
    private Data data;

    /**
     * @IsMust false
     * @Describe 该字段提供了结果内容的HTML5 页面,客户端可以无需解析处理直 接展现
     */
    private WebPage webPage;

    /**
     * @IsMust false
     * @Describe 对结果内容的最简化文本/图片描述,各服务自定义
     */
    private Answer answer;

    /**
     * @IsMust false
     * @Describe 结果内容的关联信息, 作为用户后续交互的引导展现
     */
    private Tips tips;

    /**
     * @IsMust false
     * @Describe 在存在多个候选结果时, 用于提供更多的结果描述
     */
    private UnderstandResponse[] moreResults;
}
