package com.elianshang.bridge.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("xue" , "qdqwdqwdqdqw  jinijinijij");
        if (intent.getAction().equals("com.elianshang.wms.rf.scannerservice.broadcast")) {
            String code = intent.getStringExtra("scannerdata");
            ScanManager.get().call(code);
        }
    }
}
