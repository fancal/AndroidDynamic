package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.xue.http.impl.DataHull;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登陆按钮
     */
    private Button loginButton;

    /**
     * 用户名输入框
     */
    private ContentEditText userNameEditText;

    /**
     * 密码输入框
     */
    private ContentEditText passWdEditText;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 输入框工具
     */
    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        loginButton = (Button) findViewById(R.id.login_Button);
        userNameEditText = (ContentEditText) findViewById(R.id.userName_EditText);
        passWdEditText = (ContentEditText) findViewById(R.id.passWd_EditText);

        loginButton.setOnClickListener(this);

        scanEditTextTool = new ScanEditTextTool(this, userNameEditText, passWdEditText);
        scanEditTextTool.setComplete(this);
        initToolBar();

        userNameEditText.setText("xueliyu");
        passWdEditText.setText("123456");
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
    protected void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
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

    @Override
    public void onComplete() {
        loginButton.setEnabled(true);
    }

    @Override
    public void onError(ContentEditText editText) {
        loginButton.setEnabled(false);
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
