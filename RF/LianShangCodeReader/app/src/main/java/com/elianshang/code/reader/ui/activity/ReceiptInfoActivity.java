package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ReceiptGetOrderInfo;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReceiptInfoActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Activity activity, String orderId, String containerId, String barCode, ReceiptGetOrderInfo receiptGetOrderInfo) {
        Intent intent = new Intent(activity, ReceiptInfoActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("containerId", containerId);
        intent.putExtra("barCode", barCode);
        intent.putExtra("info", receiptGetOrderInfo);
        activity.startActivityForResult(intent, 1);
    }

    private String orderId;

    private String containerId;

    private String barCode;

    private ReceiptGetOrderInfo receiptGetOrderInfo;

    private TextView productNameTextView;

    private TextView packageUnitTextView;

    private TextView orderQtyTextView;

    private ContentEditText realityNumEditView;

    private ContentEditText dateEditView;

    private ContentEditText batchIdEditView;

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiptinfo);

        readExtra();
        findView();
        fillData();
    }

    private void findView() {
        productNameTextView = (TextView) findViewById(R.id.product_name);
        packageUnitTextView = (TextView) findViewById(R.id.package_unit);
        orderQtyTextView = (TextView) findViewById(R.id.order_qty);
        realityNumEditView = (ContentEditText) findViewById(R.id.reality_num);
        dateEditView = (ContentEditText) findViewById(R.id.date_edittext);
        batchIdEditView = (ContentEditText) findViewById(R.id.batchid_edittext);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    private void readExtra() {
        Intent intent = getIntent();
        receiptGetOrderInfo = (ReceiptGetOrderInfo) intent.getSerializableExtra("info");
        orderId = intent.getStringExtra("orderId");
        containerId = intent.getStringExtra("containerId");
        barCode = intent.getStringExtra("barCode");
    }

    private void fillData() {
        productNameTextView.setText(receiptGetOrderInfo.getSkuName());
        packageUnitTextView.setText(receiptGetOrderInfo.getPackUnit());
        orderQtyTextView.setText(String.valueOf(receiptGetOrderInfo.getOrderQty()));
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            submit();
        }
    }

    private void submit() {
        String realityNum = realityNumEditView.getText().toString();
        String date = dateEditView.getText().toString();
        String batchId = batchIdEditView.getText().toString();

        if (TextUtils.isEmpty(realityNum)) {
            Toast.makeText(this, "请填入收货数量", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "请填入生产日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(batchId)) {
            Toast.makeText(this, "请填入批次号", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barCode", barCode);
            jsonObject.put("proTime", date);
            jsonObject.put("lotNum", batchId);
            jsonObject.put("inboundQty", realityNum);

            jsonArray.put(jsonObject);
        } catch (Exception e) {

        }

        new RequestReceiptAddTask(this, orderId, containerId, null, null, jsonArray.toString()).start();
    }

    private class RequestReceiptAddTask extends HttpAsyncTask<ResponseState> {

        /**
         * 物美订单编号
         */
        String orderOtherId;

        /**
         * 预约单号
         */
        String bookingNum;

        /**
         * 托盘码(扫描获得)
         */
        String containerId;

        /**
         * 收货码头
         */
        String receiptWharf;

        /**
         * 收货明细数组
         */
        String items = "items";


        public RequestReceiptAddTask(Context context, String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
            super(context, true, true);
            this.orderOtherId = orderOtherId;
            this.bookingNum = bookingNum;
            this.containerId = containerId;
            this.receiptWharf = receiptWharf;
            this.items = items;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.receiptAdd(orderOtherId, containerId, bookingNum, receiptWharf, items);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }
}
