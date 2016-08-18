package com.elianshang.wms.rf;

import android.app.Application;
import android.text.TextUtils;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.wms.rf.asyn.UserSaveTask;
import com.elianshang.wms.rf.bean.User;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    private String deviceId;

    private String imei;

    private User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ScanManager.init(this);
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

    public boolean isLogin() {
        String uToken = getUserToken();
        if (TextUtils.isEmpty(uToken)) {
            return false;
        }

        return true;
    }

    public void setUser(User user) {
        mUser = user;
        new UserSaveTask(user).start();
    }

    public User getUser() {
        if (mUser == null) {
            mUser = PreferencesManager.get().getUser();
            if (mUser == null) {
                mUser = new User();
            }
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