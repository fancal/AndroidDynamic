package com.elianshang.wms.app.receipt.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.receipt.R;

public class ChooseActivity extends DLBasePluginActivity {

    private String uId;

    private String uToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        if (readExtras()) {
            findView();
        }
    }

    private void findView() {

    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "131370164694198";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }
}
