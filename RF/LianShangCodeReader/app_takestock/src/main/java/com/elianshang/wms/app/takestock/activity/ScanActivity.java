package com.elianshang.wms.app.takestock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.takestock.R;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.AssignProvider;
import com.elianshang.wms.app.takestock.provider.ViewProvider;
import com.xue.http.impl.DataHull;

public class ScanActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, String type) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ScanActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("type", type);
        activity.startPluginActivity(intent);
    }

    private Toolbar mToolbar;

    private String uId;

    private String uToken;

    private String type;

    private TextView titleTextView;

    private TextView subTitleTextView;

    private ScanEditText codeEditText;

    private ScanEditTextTool scanEditTextTool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takestock_activity_scan);
        if (readExtras()) {
            findView();
            initToolbar();
            fillData();
        }
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
            codeEditText.setText("");
            codeEditText.requestFocus();
        }
    }

    private void findView() {
        titleTextView = (TextView) findViewById(R.id.title_TextView);
        subTitleTextView = (TextView) findViewById(R.id.subTitle_TextView);
        codeEditText = (ScanEditText) findViewById(R.id.code_EditText);

        scanEditTextTool = new ScanEditTextTool(that, codeEditText);
        scanEditTextTool.setComplete(this);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        type = getIntent().getStringExtra("type");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillData() {
        if ("LS".equals(type)) {
            titleTextView.setText("临时盘点");
            subTitleTextView.setText("请扫描库位");
            codeEditText.setCode(true);
        } else if ("JH".equals(type)) {
            titleTextView.setText("领取盘点任务");
            subTitleTextView.setText("请扫描盘点任务签");
            codeEditText.setCode(false);
        }
    }

    private void submit(String code) {
        if ("LS".equals(type)) {
            new StockTakingGetTask(that, code).start();
        } else if ("JH".equals(type)) {
            new StockTakingAssignTask(code).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        scanEditTextTool.release();
        scanEditTextTool = null;
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
        String code = codeEditText.getText().toString();
        submit(code);
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    /**
     * 领取盘点任务
     */
    private class StockTakingAssignTask extends HttpAsyncTask<TakeStockList> {

        private String code;

        public StockTakingAssignTask(String code) {
            super(ScanActivity.this.that, true, true, false, false);
            this.code = code;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return AssignProvider.request(context, uId, uToken, code);
        }

        @Override
        public void onPostExecute(TakeStockList result) {
            TakeStockActivity.launch(ScanActivity.this, uId, uToken, result, null);
        }
    }

    private class StockTakingGetTask extends HttpAsyncTask<TakeStockDetail> {

        private String locationCode;

        public StockTakingGetTask(Context context, String locationCode) {
            super(context, true, true);
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<TakeStockDetail> doInBackground() {
            return ViewProvider.request(context, uId, uToken, null, locationCode);
        }

        @Override
        public void onPostExecute(TakeStockDetail result) {
            TakeStockActivity.launch(ScanActivity.this, uId, uToken, null, result);
        }
    }
}
