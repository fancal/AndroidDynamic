package com.elianshang.wms.app.qc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.wms.app.qc.R;
import com.elianshang.wms.app.qc.controller.QcControllerProxy;
import com.ryg.dynamicload.DLBasePluginActivity;


public class QualityControlActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener {

    private QcControllerProxy mQcControllerProxy;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualitycontrol);

        mQcControllerProxy = new QcControllerProxy(this );

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ScanManager.get() != null) {
            ScanManager.get().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ScanManager.get() != null) {
            ScanManager.get().removeListener(this);
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        mQcControllerProxy.OnBarCodeReceived(s);
    }
}