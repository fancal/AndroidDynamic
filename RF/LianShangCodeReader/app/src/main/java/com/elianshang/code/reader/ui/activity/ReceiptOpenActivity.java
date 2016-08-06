package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ReceiptInfo;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * 收货扫描页,扫描 订单号,托盘码,商品barcode
 */
public class ReceiptOpenActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ReceiptOpenActivity.class);
        context.startActivity(intent);
    }

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiptopen);

        findViews();
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
        submitButton.setClickable(false);
        scanEditTextTool = new ScanEditTextTool(this, orderOtherIdEditText, containerIdEditText, barCodeEditText);
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
        new RequestGetOrderInfoTask(ReceiptOpenActivity.this, orderStr, tuoStr, productStr).start();
    }

    private void pressBack() {
        String orderOtherId = orderOtherIdEditText.getText().toString().trim();
        String containerId = containerIdEditText.getText().toString().trim();
        String barCode = barCodeEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(orderOtherId) || !TextUtils.isEmpty(containerId) || !TextUtils.isEmpty(barCode)) {
            DialogTools.showTwoButtonDialog(this, "退出将清除已经输入的内容,确定离开吗", "取消", "确定", null, new DialogInterface.OnClickListener() {
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

    private class RequestGetOrderInfoTask extends HttpAsyncTask<ReceiptInfo> {

        private String orderOtherId;

        private String containerId;

        private String barCode;

        public RequestGetOrderInfoTask(Context context, String orderOtherId, String containerId, String barCode) {
            super(context, true, true);
            this.orderOtherId = orderOtherId;
            this.containerId = containerId;
            this.barCode = barCode;
        }

        @Override
        public DataHull<ReceiptInfo> doInBackground() {
            return HttpApi.receiptGetOrdeInfo(orderOtherId, containerId, barCode);
        }

        @Override
        public void onPostExecute(int updateId, ReceiptInfo result) {
            ReceiptInfoActivity.launch(ReceiptOpenActivity.this, orderOtherId, containerId, barCode, result);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

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
        submitButton.setEnabled(true);
        submitButton.setClickable(true);
    }

    @Override
    public void onInputError(int i) {
        submitButton.setEnabled(false);
        submitButton.setClickable(false);
    }
}
