package com.elianshang.wms.app.back.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.bean.BackList;

public class BackActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, BackList backList) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ScanActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (backList != null) {
            intent.putExtra("backList", backList);
        }
        activity.startPluginActivityForResult(intent, 1);
    }

    private Toolbar mToolbar;

    private String uId;

    private String uToken;

    private BackList backList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void findView() {

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        uId = "2";
        uToken = "1231231231231";
        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        backList = (BackList) getIntent().getSerializableExtra("backList");

        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
    }
}
