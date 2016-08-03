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
    private Button checkBtn;
    private Button receiptBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();
    }

    private void findViews(){
        loginBtn = (Button) findViewById(R.id.login);
        checkBtn = (Button) findViewById(R.id.ckeck);
        receiptBtn = (Button) findViewById(R.id.receipt);
        loginBtn.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
        receiptBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            LoginActivity.launch(this);
        } else if (v == checkBtn) {
            CheckinActivity.launch(this);
        } else if(v == receiptBtn) {
            ReceiptInfoActivity.launch(this);
        }

    }
}
