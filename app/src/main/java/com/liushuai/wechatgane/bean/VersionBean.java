package com.liushuai.wechatgane.bean;

/**
 * Created by tongwenwen on 2017/12/30.
 */

public class VersionBean {

    public int version;
    public String title;
    public String content;
    public String url;
    public int type = 0;// 0 ,非强制， 1 强制更新

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
