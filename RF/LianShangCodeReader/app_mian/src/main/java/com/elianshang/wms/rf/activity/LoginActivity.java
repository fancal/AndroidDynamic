package com.elianshang.wms.rf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.tools.UITool;
import com.elianshang.wms.rf.BaseApplication;
import com.elianshang.wms.rf.PreferencesManager;
import com.elianshang.wms.rf.R;
import com.elianshang.wms.rf.bean.User;
import com.elianshang.wms.rf.provider.LoginProvider;
import com.xue.http.impl.DataHull;

import static com.elianshang.bridge.tool.HostTool.hosts;

/**
 * 登录页面
 */
public class LoginActivity extends Activity implements View.OnClickListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, 1);
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

    private View warehouseLayout;

    private TextView warehouseTextView;

    private TextView chooseButton;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 输入框工具
     */
    private ScanEditTextTool scanEditTextTool;

    private boolean isItemClick = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        checkHost();
    }

    private void findViews() {
        loginButton = (Button) findViewById(R.id.login_Button);
        userNameEditText = (ContentEditText) findViewById(R.id.userName_EditText);
        passWdEditText = (ContentEditText) findViewById(R.id.passWd_EditText);
        warehouseLayout = findViewById(R.id.warehouse_Layout);
        warehouseTextView = (TextView) findViewById(R.id.warehouse_TextView);
        chooseButton = (TextView) findViewById(R.id.choose_Button);

        loginButton.setOnClickListener(this);
        chooseButton.setOnClickListener(this);

        scanEditTextTool = new ScanEditTextTool(this, userNameEditText, passWdEditText);
        scanEditTextTool.setComplete(this);
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

    private void checkHost() {
        if (hosts.length == 1) {
            warehouseLayout.setVisibility(View.GONE);
            return;
        }

        warehouseLayout.setVisibility(View.VISIBLE);

        String hostUrl = PreferencesManager.get().getHost();
        if (!TextUtils.isEmpty(hostUrl)) {
            for (HostTool.HostElement hostElement : hosts) {
                if (TextUtils.equals(hostElement.getHostUrl(), hostUrl)) {
                    HostTool.curHost = hostElement;
                    warehouseTextView.setText("当前仓库：" + hostElement.getHostName());
                    return;
                }
            }
        }

        showChooseDialog();
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

        if (v == loginButton) {
            String userName = userNameEditText.getText().toString().trim();
            String passWd = passWdEditText.getText().toString().trim();
            new RequestLoginTask(LoginActivity.this, userName, passWd).start();
        } else if (v == chooseButton) {
            showChooseDialog();
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

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("选择仓库");

        final AlertDialog dialog = builder.create();

        final ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new WindowManager.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(linearLayout);

        for (int i = 0; i < hosts.length; i++) {
            final TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UITool.dipToPx(this, 60));
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xff000000);
            textView.setBackgroundResource(R.drawable.white_button_bg);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            final HostTool.HostElement hostElement = hosts[i];
            textView.setText(hostElement.getHostName());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HostTool.curHost = hostElement;
                    PreferencesManager.get().setHost(hostElement.getHostUrl());
                    warehouseTextView.setText("当前仓库：" + hostElement.getHostName());
                    dialog.cancel();
                }
            });

            linearLayout.addView(textView);
        }
        dialog.setView(scrollView);
        dialog.show();
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
            DataHull<User> dataHull = LoginProvider.request(context, phone, password);
            return dataHull;
        }

        @Override
        public void onPostExecute(User result) {
            BaseApplication.get().setUser(result);
            setResult(RESULT_OK);
            finish();
        }
    }
}
