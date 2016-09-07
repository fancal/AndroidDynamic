package com.elianshang.wms.rf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShareReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if ("com.elianshang.user.active".equals(action)) {//跟新user活跃时间点
                BaseApplication.get().activeUser();
            }
        }
    }
}
