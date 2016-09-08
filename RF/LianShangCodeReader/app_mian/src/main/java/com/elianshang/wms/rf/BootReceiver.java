package com.elianshang.wms.rf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.elianshang.bridge.asyn.SimpleAsyncTask;
import com.elianshang.bridge.tool.ScanManager;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("xue" , "开机");
        if (!ScanManager.get().isOpen()) {
            new ScanOpenTask().start();
        }
    }


    private class ScanOpenTask extends SimpleAsyncTask<Void> {

        @Override
        public void onCancelled() {
        }

        @Override
        public Void doInBackground() {
            ScanManager.get().open();
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
        }
    }
}
