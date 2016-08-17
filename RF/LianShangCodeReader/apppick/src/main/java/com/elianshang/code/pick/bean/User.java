package com.elianshang.code.pick.bean;

import com.xue.http.hook.BaseBean;

public class User implements BaseBean {

    private String uid;

    private String username;

    private String cellphone;

    private String status;//0为初始状态,1为正常

    private String token;

    private String jsonData;

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
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
