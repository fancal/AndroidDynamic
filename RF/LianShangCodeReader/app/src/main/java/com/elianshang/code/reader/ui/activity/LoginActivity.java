package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.BaseActivity;
import com.xue.http.impl.DataHull;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private Button loginButton;
    private EditText userNameEditText;
    private EditText passWdEditText;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        loginButton = (Button) findViewById(R.id.login_Button);
        userNameEditText = (EditText) findViewById(R.id.userName_EditText);
        passWdEditText = (EditText) findViewById(R.id.passWd_EditText);

        loginButton.setOnClickListener(this);

        userNameEditText.addTextChangedListener(this);
        passWdEditText.addTextChangedListener(this);

        initToolBar();
    }

    private void initToolBar() {
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
        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String userName = userNameEditText.getText().toString().trim();
        String passWd = passWdEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWd)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String userName = userNameEditText.getText().toString().trim();
            String passWd = passWdEditText.getText().toString().trim();
            new RequestLoginTask(LoginActivity.this, userName, passWd).start();
        }
    }

    private class RequestLoginTask extends HttpAsyncTask<User> {

        private String phone;

        private String password;

        public RequestLoginTask(Context context, String phone, String password) {
            super(context, true, true);
            this.phone = phone;
            this.password = password;
        }

        @Override
        public DataHull<User> doInBackground() {
            DataHull<User> dataHull = HttpApi.userInfoLogin(phone, password);
            return dataHull;
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            BaseApplication.get().setUser(result);
            finish();
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }
}
