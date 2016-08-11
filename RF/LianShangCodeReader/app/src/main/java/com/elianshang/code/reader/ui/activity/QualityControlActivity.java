package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.controller.QcControllerProxy;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;


public class QualityControlActivity extends BaseActivity implements ScanManager.OnBarCodeListener {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, QualityControlActivity.class);
        activity.startActivity(intent);
    }


    private QcControllerProxy mQcControllerProxy;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualitycontrol);

        mQcControllerProxy = new QcControllerProxy(this );

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }


    @Override
    public void OnBarCodeReceived(String s) {
        mQcControllerProxy.OnBarCodeReceived(s);
    }
}