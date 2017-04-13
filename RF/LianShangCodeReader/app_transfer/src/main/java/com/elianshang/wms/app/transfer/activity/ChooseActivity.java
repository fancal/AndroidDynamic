package com.elianshang.wms.app.transfer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.transfer.R;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ChooseActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Button orderReceiptButton;

    private Button storeReceiptButton;

    private boolean isItemClick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity_choose);

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

        if (orderReceiptButton == v) {
            MainActivity.launch(this, uId, uToken);
            finish();
        } else if (storeReceiptButton == v) {
            TransferActivity.launch(this, uId, uToken);
            finish();
        }
    }
}
