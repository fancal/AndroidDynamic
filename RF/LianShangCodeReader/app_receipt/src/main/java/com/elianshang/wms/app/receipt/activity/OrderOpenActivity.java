package com.elianshang.wms.app.receipt.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.receipt.R;
import com.elianshang.wms.app.receipt.bean.OrderReceiptInfo;
import com.elianshang.wms.app.receipt.provider.OrderInfoProvider;
import com.xue.http.impl.DataHull;

/**
 * 收货扫描页,扫描 订单号,托盘码,商品barcode
 */
public class OrderOpenActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), OrderOpenActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    /**
     * 订单号扫描输入框
     */
    private ScanEditText orderOtherIdEditText;

    /**
     * 托盘码扫描输入框
     */
    private ScanEditText containerIdEditText;

    /**
     * 国条码扫描输入框
     */
    private ScanEditText barCodeEditText;

    /**
     * 提交按钮
     */
    private Button submitButton;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 输入框工具
     */
    private ScanEditTextTool scanEditTextTool;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity_orderopen);

        if (readExtras()) {
            findViews();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
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
            containerIdEditText.setText("");
            barCodeEditText.setText("");

            containerIdEditText.requestFocus();
        }
    }

    private void findViews() {
        orderOtherIdEditText = (ScanEditText) findViewById(R.id.orderOtherId_EditText);
        containerIdEditText = (ScanEditText) findViewById(R.id.containerId_EditText);
        barCodeEditText = (ScanEditText) findViewById(R.id.barCode_EditText);
        submitButton = (Button) findViewById(R.id.submit_Button);

        submitButton.setEnabled(false);
        scanEditTextTool = new ScanEditTextTool(that, orderOtherIdEditText, containerIdEditText, barCodeEditText);
        scanEditTextTool.setComplete(this);

        submitButton.setOnClickListener(this);

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

    private void submit() {
        String orderStr = orderOtherIdEditText.getText().toString().trim();
        String tuoStr = containerIdEditText.getText().toString().trim();
        String productStr = barCodeEditText.getText().toString().trim();
        new RequestGetOrderInfoTask(that, orderStr, tuoStr, productStr).start();
    }

    private void pressBack() {
        String orderOtherId = orderOtherIdEditText.getText().toString().trim();
        String containerId = containerIdEditText.getText().toString().trim();
        String barCode = barCodeEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(orderOtherId) || !TextUtils.isEmpty(containerId) || !TextUtils.isEmpty(barCode)) {
            DialogTools.showTwoButtonDialog(that, "退出将清除已经输入的内容,确定离开吗", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            submit();
        }
    }

    private class RequestGetOrderInfoTask extends HttpAsyncTask<OrderReceiptInfo> {

        private String orderOtherId;

        private String containerId;

        private String barCode;

        public RequestGetOrderInfoTask(Context context, String orderOtherId, String containerId, String barCode) {
            super(context, true, true, true);
            this.orderOtherId = orderOtherId;
            this.containerId = containerId;
            this.barCode = barCode;
        }

        @Override
        public DataHull<OrderReceiptInfo> doInBackground() {
            return OrderInfoProvider.request(context, uId, uToken, orderOtherId, containerId, barCode);
        }

        @Override
        public void onPostExecute(OrderReceiptInfo result) {
            OrderInfoActivity.launch(OrderOpenActivity.this, uId, uToken, orderOtherId, containerId, barCode, result);
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);

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
        submitButton.setEnabled(true);
    }

    @Override
    public void onError(ContentEditText editText) {
        submitButton.setEnabled(false);
    }
}
