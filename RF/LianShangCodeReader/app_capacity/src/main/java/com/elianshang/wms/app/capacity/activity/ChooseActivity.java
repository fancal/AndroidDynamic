package com.elianshang.wms.app.capacity.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.capacity.R;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button mergeButton;

    private Button partButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_activity_choose);

        if (readExtras()) {
            findView();
        }
    }

    private void findView() {
        mergeButton = (Button) findViewById(R.id.merge_Button);
        partButton = (Button) findViewById(R.id.part_Button);

        mergeButton.setOnClickListener(this);
        partButton.setOnClickListener(this);
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "2";
//        uToken = "131133941499842";
//        ScanManager.init(that);
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (mergeButton == v) {
            CapacityMergeActivity.launch(this, uId, uToken);
            finish();
        } else if (partButton == v) {
            CapacityPartActivity.launch(this, uId, uToken);
            finish();
        }
    }
}
