package com.elianshang.code.pick;

import android.app.Application;
import android.text.TextUtils;

import com.elianshang.bridge.http.HttpApi;
import com.elianshang.bridge.http.HttpDynamicHeader;
import com.elianshang.bridge.tool.AppTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.code.pick.asyn.UserSaveTask;
import com.elianshang.code.pick.bean.User;
import com.elianshang.code.pick.db.PreferencesManager;
import com.elianshang.code.pick.tool.ConfigTool;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DefaultKVPBean;

import java.util.ArrayList;
import java.util.List;


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

        initHttp();
    }

    private void initHttp() {
        HttpApi.build(ConfigTool.getHttpBaseUrl(), AppTool.getAppVersion(this), AppTool.getAppVersion(this), new HttpDynamicHeader() {
            @Override
            public List<BaseKVP> getDynamicHeader() {
                List<BaseKVP> dynamicHeader = new ArrayList<>();
                dynamicHeader.add(new DefaultKVPBean("uid", getUserId()));
                dynamicHeader.add(new DefaultKVPBean("utoken", getUserToken()));
                return dynamicHeader;
            }
        });
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