package com.elianshang.wms.rf.bean;

import com.xue.http.hook.BaseBean;

public class User implements BaseBean {

    private String uid;

    private String userName;

    private String token;

    private long activeTime ;

    private long validTime ;

    private String jsonData;

    public void setToken(String token) {
        this.token = token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getToken() {
        return token;
    }

    public String getUid() {
        return uid;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return jsonData;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

}
