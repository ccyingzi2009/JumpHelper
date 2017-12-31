package com.liushuai.wechatgane;

/**
 * Created by tongwenwen on 2017/12/30.
 */

public class Constant {

    public final static String BASEURL = "https://jump-helper-1255781935.cos.ap-beijing.myqcloud.com/";
    public final static String URL_SOURCE = BASEURL + "jumpHelper.apk";
    public final static String URL_VERSION = BASEURL + "version.json";


    public final static String PATH_APK_DIR = BaseApplication.getInstance().getFilesDir().getAbsolutePath();
    public final static String PARAM_APK_NAME = "helper.apk";
}
