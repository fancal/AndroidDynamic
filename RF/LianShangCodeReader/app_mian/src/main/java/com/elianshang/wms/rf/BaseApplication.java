package com.elianshang.wms.rf;

import android.app.Application;
import android.text.TextUtils;

import com.elianshang.bridge.tool.HostTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.wms.rf.asyn.UserSaveTaskSimple;
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
        new UserSaveTaskSimple(user).start();
    }

    public void activeUser() {
        if (mUser != null) {
            mUser.setActiveTime(System.currentTimeMillis());
            new UserSaveTaskSimple(mUser).start();
        }
    }

    public User getUser() {
        if (mUser == null) {
            mUser = PreferencesManager.get().getUser();
            if (mUser == null) {
                mUser = new User();
            }

            getHost();
        }

        return mUser;
    }

    private void getHost() {
        String hostUrl = PreferencesManager.get().getHost();
        if (!TextUtils.isEmpty(hostUrl)) {
            for (HostTool.HostElement hostElement : HostTool.hosts) {
                if (TextUtils.equals(hostElement.getHostUrl(), hostUrl)) {
                    HostTool.curHost = hostElement;
                    return;
                }
            }
        }

        mUser = new User();
    }

    public String getUserId() {
        return getUser().getUid();
    }

    public String getUserToken() {
        return getUser().getToken();
    }

}