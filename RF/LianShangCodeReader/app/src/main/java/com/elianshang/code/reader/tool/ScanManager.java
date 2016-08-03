package com.elianshang.code.reader.tool;

import android.barcode.BarCodeManager;
import android.content.Context;

public class ScanManager {

    private static ScanManager scanManager;

    private static boolean isInit = false;

    private BarCodeManager mBarCode;

    private ScanManager(Context context) {
        mBarCode = (BarCodeManager) context.getSystemService("barcode");
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

    public void addListener(BarCodeManager.OnBarCodeReceivedListener listener) {
        if (listener == null) {
            return;
        }

        if (mBarCode != null) {
            mBarCode.addListener(listener);
        }
    }

    public void removeListener(BarCodeManager.OnBarCodeReceivedListener listener) {
        if (listener == null) {
            return;
        }

        if (mBarCode != null) {
            mBarCode.removeListener(listener);
        }
    }
}
