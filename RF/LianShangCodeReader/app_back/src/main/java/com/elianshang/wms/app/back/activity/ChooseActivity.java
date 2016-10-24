package com.elianshang.wms.app.back.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.back.R;

/**
 * Created by liuhanzhi on 16/10/24.
 */

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button transferInButton;

    private Button shelveButton;

    private Button transferOutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        if (readExtras()) {
            findViews();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        //FIXME
        uId = "141871359725260";
        uToken = "25061134202027";
        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findViews() {
        transferInButton = (Button) findViewById(R.id.transferIn_Button);
        shelveButton = (Button) findViewById(R.id.shelve_Button);
        transferOutButton = (Button) findViewById(R.id.transferOut_Button);

        transferInButton.setOnClickListener(this);
        shelveButton.setOnClickListener(this);
        transferOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == transferInButton) {
            TransferInActivity.launch(ChooseActivity.this, uId, uToken);
        } else if (v == shelveButton) {
            ShelveActivity.launch(ChooseActivity.this, uId, uToken);
        } else if (v == transferOutButton) {
            TransferOutActivity.launch(ChooseActivity.this, uId, uToken);
        }
    }
}
