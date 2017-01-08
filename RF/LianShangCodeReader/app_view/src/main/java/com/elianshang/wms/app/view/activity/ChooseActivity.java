package com.elianshang.wms.app.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.view.R;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button skuViewButton;

    private Button locationViewButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity_choose);

        if (readExtras()) {
            findView();
        }
    }

    private void findView() {
        skuViewButton = (Button) findViewById(R.id.skuView_Button);
        locationViewButton = (Button) findViewById(R.id.locationView_Button);

        skuViewButton.setOnClickListener(this);
        locationViewButton.setOnClickListener(this);
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "1";
//        uToken = "198302935052918";
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (skuViewButton == v) {
//            OrderOpenActivity.launch(this, uId, uToken);
            finish();
        } else if (locationViewButton == v) {
//            StoreOpenActivity.launch(this, uId, uToken);
            finish();
        }
    }
}