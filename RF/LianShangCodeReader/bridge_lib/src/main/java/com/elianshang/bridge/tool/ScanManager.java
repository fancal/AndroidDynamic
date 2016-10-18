package com.elianshang.bridge.tool;

import android.app.Activity;
import android.barcode.BarCodeManager;
import android.content.Context;
import android.util.Log;

import com.elianshang.bridge.R;
import com.elianshang.tools.SoundManager;

import java.util.ArrayList;

public class ScanManager {

    private static ScanManager scanManager;

    private static boolean isInit = false;

    private BarCodeManager mBarCode;

    private BarCodeManager.OnBarCodeReceivedListener mainListener;

    private ArrayList<OnBarCodeListener> listeners;

    private boolean isOpen = false;

    private SoundManager soundManager;

    private ScanManager(final Context context) {

        if (checkClass()) {
            mBarCode = (BarCodeManager) context.getSystemService("barcode");
            mBarCode.setBarCodeWaitTime(100);

            soundManager = new SoundManager(context, R.raw.beep);

            mainListener = new BarCodeManager.OnBarCodeReceivedListener() {
                @Override
                public void OnBarCodeReceived(String s) {
                    soundManager.playBeepSoundAndVibrate();
                    if (listeners != null && listeners.size() > 0) {
                        for (OnBarCodeListener onBarCodeListener : listeners) {
                            try {
                                onBarCodeListener.OnBarCodeReceived(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    public synchronized void openSoundControl(Activity activity) {
        soundManager.openSoundControl(activity);
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

    public void open() {
        if (mBarCode != null) {
            if (!isOpen) {
                mBarCode.open();
                isOpen = true;
            }
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void addListener(OnBarCodeListener listener) {
        if (listener == null) {
            return;
        }

        if (listeners == null) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(OnBarCodeListener listener) {
        if (listener == null) {
            return;
        }

        if (listeners == null) {
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
