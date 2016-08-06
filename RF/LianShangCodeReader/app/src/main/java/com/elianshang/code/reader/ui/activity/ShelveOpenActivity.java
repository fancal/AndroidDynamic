package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.Shelve;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * 上架进入页
 */
public class ShelveOpenActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ShelveOpenActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 托盘码扫描输入框
     */
    private ScanEditText containerIdEditText;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelveopen);

        findView();
    }

    private void findView() {
        containerIdEditText = (ScanEditText) findViewById(R.id.containerId_EditText);

        scanEditTextTool = new ScanEditTextTool(this, containerIdEditText);
        scanEditTextTool.setComplete(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressBack();
            }
        });
    }

    private void pressBack() {
        String containerId = containerIdEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(containerId)) {
            DialogTools.showOneButtonDialog(this, "请完成任务,不要退出", "知道了", null, false);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onSetComplete() {
        String containerId = containerIdEditText.getText().toString();

        new ShelveScanContainerTask(this, containerId).start();
    }

    @Override
    public void onInputError(int i) {

    }

    private class ShelveScanContainerTask extends HttpAsyncTask<Shelve> {

        private String containerId;

        public ShelveScanContainerTask(Context context, String containerId) {
            super(context, true, true);
            this.containerId = containerId;
        }

        @Override
        public DataHull<Shelve> doInBackground() {
            HttpApi.shelveCreateTask(containerId);
            return HttpApi.shelveScanContainer(BaseApplication.get().getUserId(), containerId);
        }

        @Override
        public void onPostExecute(int updateId, Shelve result) {
            ShelveFinishActivity.launch(ShelveOpenActivity.this, result);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }
}
