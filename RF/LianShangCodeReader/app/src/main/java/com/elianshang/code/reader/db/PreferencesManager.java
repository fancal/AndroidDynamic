package com.elianshang.code.reader.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.elianshang.code.reader.BaseApplication;


/**
 * Created by liuhanzhi on 15/11/28.
 */
public class PreferencesManager {

    private static PreferencesManager mInstance;
    private Context mContext;

    private static String DEVICE = "device";

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

}
