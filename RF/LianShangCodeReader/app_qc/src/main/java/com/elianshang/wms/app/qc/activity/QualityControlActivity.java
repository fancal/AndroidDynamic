package com.elianshang.wms.app.qc.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.qc.R;
import com.elianshang.wms.app.qc.controller.QcControllerProxy;


public class QualityControlActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener {

    private String uId;

    private String uToken;

    private QcControllerProxy mQcControllerProxy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualitycontrol);
        if (readExtras()) {
            mQcControllerProxy = new QcControllerProxy(that, uId, uToken);
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "198719546871260";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会重新开始", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
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
    public void onDestroy() {
        super.onDestroy();
        if (mQcControllerProxy != null) {
            mQcControllerProxy.releaseScanEditTextTool();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        mQcControllerProxy.OnBarCodeReceived(s);
    }
}