package com.elianshang.bridge.tool;

import android.barcode.BarCodeManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

public class ScanManager {

    private static ScanManager scanManager;

    private static boolean isInit = false;

    private BarCodeManager mBarCode;

    private BarCodeManager.OnBarCodeReceivedListener mainListener;

    private ArrayList<OnBarCodeListener> listeners;

    private ScanManager(final Context context) {

        if (checkClass()) {
            mBarCode = (BarCodeManager) context.getSystemService("barcode");

            mainListener = new BarCodeManager.OnBarCodeReceivedListener() {
                @Override
                public void OnBarCodeReceived(String s) {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                    if (listeners != null && listeners.size() > 0) {
                        for (OnBarCodeListener onBarCodeListener : listeners) {
                            onBarCodeListener.OnBarCodeReceived(s);
                        }
                    }
                }
            };
            mBarCode.addListener(mainListener);
        } else {
            Log.e("xue", "类不存在");
        }

        listeners = new ArrayList();
    }

    public synchronized static void init(Context context) {
        if (isInit) {
            return;
        }

        scanManager = new ScanManager(context);
        isInit = true;
    }

    public static ScanManager get() {
        if (!isInit) {
            return null;
        }
        return scanManager;
    }

    public void addListener(OnBarCodeListener listener) {
        if (listener == null) {
            return;
        }

        if(listeners == null){
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(OnBarCodeListener listener) {
        if (listener == null) {
            return;
        }

        if(listeners == null){
            return;
        }

        listeners.remove(listener);
    }

    private boolean checkClass() {
        try {
            Class aClass = Class.forName("android.barcode.BarCodeManager");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static interface OnBarCodeListener {
        public void OnBarCodeReceived(String s);
    }
}
