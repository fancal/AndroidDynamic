package com.elianshang.code.reader.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.BaseActivity;

/**
 * Created by wangwenwang on 16/7/28.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    private Button loginBtn;
    private Button receiptBtn;
    private Button shelveBtn;
    private Button receiveTaskBtn;
    private Button finishOperationBtn;
    private Button createScrap;
    private Button createReturn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();
    }

    private void findViews() {
        loginBtn = (Button) findViewById(R.id.login);
        receiptBtn = (Button) findViewById(R.id.receipt);
        shelveBtn = (Button) findViewById(R.id.shelve);
        receiveTaskBtn = (Button) findViewById(R.id.receive_task);
        finishOperationBtn = (Button) findViewById(R.id.finish_operation);
        createScrap = (Button) findViewById(R.id.create_scrap);
        createReturn = (Button) findViewById(R.id.create_return);

        loginBtn.setOnClickListener(this);
        receiptBtn.setOnClickListener(this);
        shelveBtn.setOnClickListener(this);
        receiveTaskBtn.setOnClickListener(this);
        finishOperationBtn.setOnClickListener(this);
        createScrap.setOnClickListener(this);
        createReturn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            LoginActivity.launch(this);
        } else if (v == receiptBtn) {
            ReceiptOpenActivity.launch(this);
        } else if (v == shelveBtn) {
            ShelveOpenActivity.launch(this);
        } else if (v == receiveTaskBtn) {
            ShelveOpenActivity.launch(this);
        } else if (v == finishOperationBtn) {
            ShelveFinishActivity.launch(this, null);
        } else if (v == createScrap) {
            CreateScrapActivity.launch(this);
        } else if (v == createReturn) {
            CreateReturnActivity.launch(this);
        }

    }
}
