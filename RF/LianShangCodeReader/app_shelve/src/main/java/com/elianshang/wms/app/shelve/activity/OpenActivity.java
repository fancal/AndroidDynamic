package com.elianshang.wms.app.shelve.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.shelve.R;
import com.elianshang.wms.app.shelve.bean.Shelve;
import com.elianshang.wms.app.shelve.provider.ScanContainerProvider;
import com.xue.http.impl.DataHull;

/**
 * 上架进入页
 */
public class OpenActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken, Shelve shelve) {
        DLIntent intent = new DLIntent(activity.getPackageName(), OpenActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        if (shelve != null) {
            intent.putExtra("shelve", shelve);
        }
        activity.startPluginActivity(intent);
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

    private String uId;

    private String uToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelveopen);
        if (readExtras()) {
            findView();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        Shelve shelve = (Shelve) getIntent().getSerializableExtra("shelve");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        if (shelve != null) {
            FinishActivity.launch(OpenActivity.this, uId, uToken, shelve);
        }

        return true;
    }

    private void findView() {
        containerIdEditText = (ScanEditText) findViewById(R.id.containerId_EditText);

        scanEditTextTool = new ScanEditTextTool(that, containerIdEditText);
        scanEditTextTool.setComplete(this);

        containerIdEditText.requestFocus();

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
        finish();
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ScanManager.get() != null) {
            ScanManager.get().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ScanManager.get() != null) {
            ScanManager.get().removeListener(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            containerIdEditText.setText(null);
            containerIdEditText.requestFocus();
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onComplete() {
        String containerId = containerIdEditText.getText().toString();

        new ShelveScanContainerTask(that, containerId, uId).start();
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    private class ShelveScanContainerTask extends HttpAsyncTask<Shelve> {

        private String containerId;

        private String uId;

        public ShelveScanContainerTask(Context context, String containerId, String uId) {
            super(context, true, true);
            this.containerId = containerId;
            this.uId = uId;
        }

        @Override
        public DataHull<Shelve> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(Shelve result) {
            FinishActivity.launch(OpenActivity.this, uId, uToken, result);
        }
    }
}
