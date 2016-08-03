package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ReceiptGetOrderInfo;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;


public class CheckinActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Context context) {
        Intent intent = new Intent(context, CheckinActivity.class);
        context.startActivity(intent);
    }

    private ScanEditText orderidEditText;
    private ScanEditText tuoidEditText;
    private ScanEditText productidEditText;
    private Button button;
    private Toolbar mToolbar;

    private ScanEditTextTool scanEditTextTool;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

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

    private void findViews() {
        orderidEditText = (ScanEditText) findViewById(R.id.orderid_edittext);
        tuoidEditText = (ScanEditText) findViewById(R.id.tuoid_edittext);
        productidEditText = (ScanEditText) findViewById(R.id.productid_edittext);
        button = (Button) findViewById(R.id.button);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button.setEnabled(false);
        button.setClickable(false);
        scanEditTextTool = new ScanEditTextTool(this, orderidEditText, tuoidEditText, productidEditText);
        scanEditTextTool.setComplete(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderStr = orderidEditText.getText().toString().trim();
                String tuoStr = tuoidEditText.getText().toString().trim();
                String productStr = productidEditText.getText().toString().trim();

                new RequestGetOrdeInfoTask(CheckinActivity.this, orderStr, tuoStr, productStr).start();

                ToastTool.show(CheckinActivity.this, "扫描完成提交数据");
            }
        });
    }

    private class RequestGetOrdeInfoTask extends HttpAsyncTask<ReceiptGetOrderInfo> {

        private String orderOtherId;

        private String containerId;

        private String barCode;

        public RequestGetOrdeInfoTask(Context context, String orderOtherId, String containerId, String barCode) {
            super(context, true, true);
            this.orderOtherId = orderOtherId;
            this.containerId = containerId;
            this.barCode = barCode;
        }

        @Override
        public DataHull<ReceiptGetOrderInfo> doInBackground() {
            return HttpApi.receiptGetOrdeInfo(orderOtherId, containerId, barCode);
        }

        @Override
        public void onPostExecute(int updateId, ReceiptGetOrderInfo result) {

        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onSetComplete() {
        button.setEnabled(true);
        button.setClickable(true);

    }

    @Override
    public void onInputError(int i) {
        button.setEnabled(false);
        button.setClickable(false);

    }
}
