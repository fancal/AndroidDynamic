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
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.receipt.R;
import com.elianshang.wms.app.receipt.bean.ResponseState;
import com.elianshang.wms.app.receipt.bean.StoreReceiptInfo;
import com.elianshang.wms.app.receipt.provider.AddProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 收货详情完成页
 */
public class StoreInfoActivity extends DLBasePluginActivity implements View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken, String storeId, String containerId, String orderOtherId, String barCode, StoreReceiptInfo storeReceiptInfo) {
        DLIntent intent = new DLIntent(activity.getPackageName(), StoreInfoActivity.class);
        intent.putExtra("storeId", storeId);
        intent.putExtra("containerId", containerId);
        intent.putExtra("orderOtherId", orderOtherId);
        intent.putExtra("barCode", barCode);
        intent.putExtra("storeReceiptInfo", storeReceiptInfo);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

    /**
     * 订单号,上一页传入
     */
    private String storeId;

    /**
     * 托盘号,上一页传入
     */
    private String containerId;

    private String orderOtherId;

    /**
     * 商品条码,上一页传入
     */
    private String barCode;

    /**
     * 收货商品详情,上一页传入
     */
    private StoreReceiptInfo storeReceiptInfo;

    private TextView locationTextView;

    private TextView proTimeTextView;

    /**
     * 商品名称TextView
     */
    private TextView itemNameTextView;

    /**
     * 商品包装数TextView
     */
    private TextView packNameTextView;

    /**
     * 订单商品数
     */
    private TextView orderQtyTextView;

    /**
     * 实际数量输入框
     */
    private QtyEditText inboundQtyEditView;

    /**
     * 提交按钮
     */
    private Button submitButton;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    private String serialNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storereceiptinfo);

        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        if (readExtra()) {
            findView();
            fillData();
        }
    }

    private void findView() {
        locationTextView = (TextView) findViewById(R.id.location_TextView);
        proTimeTextView = (TextView) findViewById(R.id.proTime_TextView);
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        packNameTextView = (TextView) findViewById(R.id.packName_TextView);
        orderQtyTextView = (TextView) findViewById(R.id.orderQty_TextView);
        inboundQtyEditView = (QtyEditText) findViewById(R.id.inboundQty_EditView);
        submitButton = (Button) findViewById(R.id.submit_Button);
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

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        storeReceiptInfo = (StoreReceiptInfo) intent.getSerializableExtra("storeReceiptInfo");
        storeId = intent.getStringExtra("storeId");
        containerId = intent.getStringExtra("containerId");
        orderOtherId = intent.getStringExtra("orderOtherId");
        barCode = intent.getStringExtra("barCode");

        return true;
    }

    private void fillData() {
        if (storeReceiptInfo == null) {
            return;
        }
        locationTextView.setText(storeReceiptInfo.getLocation());
        proTimeTextView.setText(storeReceiptInfo.getProTime());
        itemNameTextView.setText(storeReceiptInfo.getSkuName());
        packNameTextView.setText(storeReceiptInfo.getPackName());
        orderQtyTextView.setText(storeReceiptInfo.getOrderQty());
        inboundQtyEditView.setHint(storeReceiptInfo.getOrderQty());
        inboundQtyEditView.setText(null);
    }

    private void pressBack() {
        String inboundQty = inboundQtyEditView.getValue();

        if (!TextUtils.isEmpty(inboundQty)) {
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

    private void submit() {
        if (storeReceiptInfo == null) {
            return;
        }

        String inboundQty = inboundQtyEditView.getValue();

        if (TextUtils.isEmpty(inboundQty)) {
            Toast.makeText(that, "请填入收货数量", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barCode", barCode);
            jsonObject.put("inboundQty", inboundQty);

            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new ReceiptAddTask(that, uId, uToken, storeId, orderOtherId, containerId, null, null, jsonArray.toString()).start();
    }

    private class ReceiptAddTask extends HttpAsyncTask<ResponseState> {

        String uid;

        String uToken;
        /**
         * 物美订单编号
         */
        String storeId;

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
        String items;

        public ReceiptAddTask(Context context, String uid, String uToken, String storeId, String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
            super(context, true, true);
            this.uid = uid;
            this.uToken = uToken;
            this.storeId = storeId;
            this.orderOtherId = orderOtherId;
            this.bookingNum = bookingNum;
            this.containerId = containerId;
            this.receiptWharf = receiptWharf;
            this.items = items;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return AddProvider.request(context, uId, uToken, storeId, orderOtherId, containerId, bookingNum, receiptWharf, items, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            that.setResult(RESULT_OK);
            finish();
            ToastTool.show(context, "收货完成");
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);

        }
    }
}
