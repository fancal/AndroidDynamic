package com.elianshang.wms.app.takestock.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.takestock.R;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button interimTakeStockButton;

    private Button planTakeStockButton;

    private boolean isItemClick = false ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takestock_activity_choose);

        if (readExtras()) {
            findView();
        }
    }

    private void findView() {
        interimTakeStockButton = (Button) findViewById(R.id.interimTakeStock_Button);
        planTakeStockButton = (Button) findViewById(R.id.planTakeStock_Button);

        interimTakeStockButton.setOnClickListener(this);
        planTakeStockButton.setOnClickListener(this);
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "1";
//        uToken = "198302935052918";
//        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (isItemClick) {
            return;
        }

        isItemClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isItemClick = false;
            }
        }, 500);

        if (interimTakeStockButton == v) {
            ScanActivity.launch(this, uId, uToken, "LS");
            finish();
        } else if (planTakeStockButton == v) {
            MainActivity.launch(this, uId, uToken);
            finish();
        }
    }
}
