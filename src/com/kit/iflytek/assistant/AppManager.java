package com.kit.iflytek.assistant;

import android.content.Context;

import com.kit.extend.iflytek.R;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.utils.AppUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.MapUtils;
import com.kit.utils.ResWrapper;
import com.kit.utils.system.RootUtils;

import java.util.ArrayList;

/**
 * service为APP时候的工具类
 * <p/>
 * Created by Zhao on 16/7/21.
 */
public class AppManager {


    private static AppManager appHelper;

    public static AppManager getInstance() {
        if (appHelper == null)
            appHelper = new AppManager();

        return appHelper;
    }

    /**
     * 从 Operation.ANSWER中 滤出较为符合聊天习惯的应答(如果没有合适的,直接返回null)
     *
     * @param understandResponse
     * @return
     */
    @SuppressWarnings("unchecked")
    public UnderstandResponse dispatch(UnderstandResponse understandResponse) {
        if (understandResponse == null)
            return null;
        Context context = ResWrapper.getInstance().getContext();
        String appName;
        ArrayList<String> packageNames = null;
        if (!MapUtils.isNullOrEmpty(understandResponse.semantic.slots.dataMap)) {
            //本地拦截到
            appName = (String) understandResponse.semantic.slots.dataMap.get("appName");
            packageNames = (ArrayList<String>) understandResponse.semantic.slots.dataMap.get("packageNames");

        } else {
            appName =  understandResponse.semantic.slots.name;
        }


        if (ListUtils.isNullOrEmpty(packageNames)) {
            packageNames = AppUtils.getPackageNamesByAppName(ResWrapper.getInstance().getContext(), appName);
        }

        String replyStr;


        switch (understandResponse.operation) {
            case Operation.LAUNCH:
            case Operation.UNINSTALL:
            case Operation.EXIT:
                UnderstandResponse noFind = getNoFind(packageNames, understandResponse);
                if (noFind != null) {
                    understandResponse = noFind;
                } else {
                    String packageName;
                    if (!ListUtils.isNullOrEmpty(packageNames)
                            && packageNames.size() == 1)
                        packageName = packageNames.get(0);
                    else
                        packageName = ListUtils.getOne(packageNames);

                    switch (understandResponse.operation) {
                        case Operation.LAUNCH:
                            AppUtils.launchApk(context, packageName);
                            replyStr = String.format(ResWrapper.getInstance().getString(R.string.app_launching), appName);
                            understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);
                            break;

                        case Operation.UNINSTALL:
                            AppUtils.uninstallAPK(context, packageName);
                            replyStr = String.format(ResWrapper.getInstance().getString(R.string.app_unintalling), appName);
                            understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);
                            break;

                        case Operation.EXIT:
                            boolean isAppHaveRootPremission = RootUtils.killProcess(packageName);
                            if (isAppHaveRootPremission) {
                                replyStr = String.format(ResWrapper.getInstance().getString(R.string.app_killed), appName);
//                                RootUtils.killProcess(packageName);
                            } else {
                                replyStr = String.format(ResWrapper.getInstance().getString(R.string.app_kill_need_premission), appName);
                            }
                            understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);


                            break;

                    }

                }
                break;

            case Operation.DOWNLOAD:
            case Operation.INSTALL:
            case Operation.QUERY:
                replyStr = String.format(ResWrapper.getInstance().getString(R.string.app_searching), appName);
                understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);

                AppUtils.searchAppInMarketByKeyword(context, appName);
                break;


        }

        return understandResponse;

    }


    private UnderstandResponse getNoFind(ArrayList<String> packageNames, UnderstandResponse understandResponse) {
        if (ListUtils.isNullOrEmpty(packageNames)) {
            String appName = (String) understandResponse.semantic.slots.dataMap.get("appName");
            String replyStr = String.format(ResWrapper.getInstance().getString(R.string.no_find_app)
                    , appName);

            understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);

            return understandResponse;
        }
        return null;
    }

    public class Operation {

        /**
         * LAUNCH
         * 打开应用(缺省)
         */
        public static final String LAUNCH = "LAUNCH";

        /**
         * UNINSTALL
         * 卸载应用
         */
        public static final String UNINSTALL = "UNINSTALL";

        /**
         * DOWNLOAD
         * 下载应用
         */
        public static final String DOWNLOAD = "DOWNLOAD";

        /**
         * INSTALL
         * 安装应用
         */
        public static final String INSTALL = "INSTALL";

        /**
         * QUERY
         * 搜索应用
         */
        public static final String QUERY = "QUERY";

        /**
         * EXIT
         * 退出或关闭应用
         */
        public static final String EXIT = "EXIT";

    }
}
