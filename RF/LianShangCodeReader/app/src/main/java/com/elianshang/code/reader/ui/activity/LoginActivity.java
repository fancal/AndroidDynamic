package com.elianshang.code.reader.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.BaseActivity;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity{

    private Button mLogin;
    private EditText mName;
    private EditText mPassword;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews(){
        mLogin = (Button) findViewById(R.id.login);
        mName = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
