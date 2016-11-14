package com.elianshang.wms.app.receipt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.receipt.R;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button orderReceiptButton;

    private Button storeReceiptButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity_choose);

        if (readExtras()) {
            findView();
        }
    }

    private void findView() {
        orderReceiptButton = (Button) findViewById(R.id.orderReceipt_Button);
        storeReceiptButton = (Button) findViewById(R.id.storeReceipt_Button);

        orderReceiptButton.setOnClickListener(this);
        storeReceiptButton.setOnClickListener(this);
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "194252780886189";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (orderReceiptButton == v) {
            OrderOpenActivity.launch(this, uId, uToken);
            finish();
        } else if (storeReceiptButton == v) {
            StoreOpenActivity.launch(this, uId, uToken);
            finish();
        }
    }
}
