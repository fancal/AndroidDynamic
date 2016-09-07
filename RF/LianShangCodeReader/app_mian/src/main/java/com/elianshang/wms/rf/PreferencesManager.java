package com.elianshang.wms.rf;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.elianshang.wms.rf.bean.User;
import com.elianshang.wms.rf.parser.UserParser;

import org.json.JSONObject;

public class PreferencesManager {

    private static PreferencesManager mInstance;
    private Context mContext;

    private static String DEVICE = "device";
    private static String USER = "user";

    public static PreferencesManager get() {
        if (null == mInstance) {
            mInstance = new PreferencesManager(BaseApplication.get());
        }
        return mInstance;
    }

    private PreferencesManager(Context context) {
        this.mContext = context;
    }

    /**
     * 写入设备IMEI
     */
    public void writeIMEI(String deviceId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!TextUtils.isEmpty(deviceId)) {
            editor.putString("IMEI", deviceId).commit();
        }
    }

    /**
     * 读取设备IMEI
     */
    public String readIMEI() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DEVICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("IMEI", null);
    }

    /**
     * 设置User
     */
    public boolean setUser(User user) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (user == null) {
            return editor.putLong("activeTime", 0).putString("info", null).commit();
        }

        return editor.putLong("activeTime", user.getActiveTime()).putString("info", user.toString()).commit();
    }


    /**
     * 获取User
     */
    public User getUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        try {
            String userString = sharedPreferences.getString("info", null);
            long activeTime = sharedPreferences.getLong("activeTime", 0);
            if (!TextUtils.isEmpty(userString) && activeTime > 0) {
                User user = new UserParser().parse(new JSONObject(userString));
                long curTime = System.currentTimeMillis();
                if (user != null) {
                    if (curTime < activeTime + user.getValidTime()) {
                        user.setActiveTime(activeTime);
                    } else {
                        user = null;
                    }
                }

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
