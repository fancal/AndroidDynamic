package com.elianshang.code.reader;

import android.app.Application;
import android.text.TextUtils;

import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.db.PreferencesManager;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    private String deviceId;

    private String imei;

    private User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static BaseApplication get() {
        return mInstance;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
        if (!TextUtils.isEmpty(imei)) {
            PreferencesManager.get().writeIMEI(imei);
        }
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        if(mUser == null){
            mUser = new User();
        }
        return mUser;
    }

    public String getUserId() {
        return getUser().getUid();
    }

    public String getUserToken() {
        return getUser().getToken();
    }
}