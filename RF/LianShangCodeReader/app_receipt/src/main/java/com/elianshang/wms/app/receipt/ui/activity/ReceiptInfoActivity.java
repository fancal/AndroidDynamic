package com.elianshang.wms.app.receipt.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DateKeyboardUtil;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.receipt.R;
import com.elianshang.wms.app.receipt.bean.ReceiptInfo;
import com.elianshang.wms.app.receipt.bean.ResponseState;
import com.elianshang.wms.app.receipt.provider.ReceiptAddProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 收货详情完成页
 */
public class ReceiptInfoActivity extends DLBasePluginActivity implements View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String orderOtherId, String containerId, String barCode, ReceiptInfo receiptInfo) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ReceiptInfoActivity.class);
        intent.putExtra("orderOtherId", orderOtherId);
        intent.putExtra("containerId", containerId);
        intent.putExtra("barCode", barCode);
        intent.putExtra("info", receiptInfo);
        activity.startPluginActivityForResult(intent, 1);
    }

    /**
     * 订单号,上一页传入
     */
    private String orderOtherId;

    /**
     * 托盘号,上一页传入
     */
    private String containerId;

    /**
     * 商品条码,上一页传入
     */
    private String barCode;

    /**
     * 收货商品详情,上一页传入
     */
    private ReceiptInfo receiptInfo;

    /**
     * 商品名称TextView
     */
    private TextView itemNameTextView;

    /**
     * 商品包装数TextView
     */
    private TextView packUnitTextView;

    /**
     * 订单商品数
     */
    private TextView orderQtyTextView;

    /**
     * 实际数量输入框
     */
    private QtyEditText inboundQtyEditView;

    /**
     * 批次号布局
     */
    private View lotNumLayout;

    /**
     * 批次号输入框
     */
    private ContentEditText lotNumEditText;

    /**
     * 提交按钮
     */
    private Button submitButton;

    private KeyboardView mKeyboardView;

    /**
     * 年输入框
     */
    private EditText mEditYear;

    /**
     * 月输入框
     */
    private EditText mEditMonth;

    /**
     * 日输入框
     */
    private EditText mEditDay;

    /**
     * 键盘工具
     */
    private DateKeyboardUtil keyboardUtil;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiptinfo);

        readExtra();
        findView();
        fillData();
    }

    private void findView() {
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        packUnitTextView = (TextView) findViewById(R.id.packUnit_TextView);
        orderQtyTextView = (TextView) findViewById(R.id.orderQty_TextView);
        inboundQtyEditView = (QtyEditText) findViewById(R.id.inboundQty_EditView);
        lotNumLayout = findViewById(R.id.lotNum_Layout);
        lotNumEditText = (ContentEditText) findViewById(R.id.lotNum_EditText);
        submitButton = (Button) findViewById(R.id.submit_Button);
        mEditYear = (EditText) findViewById(R.id.et_year);
        mEditMonth = (EditText) findViewById(R.id.et_month);
        mEditDay = (EditText) findViewById(R.id.et_day);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        keyboardUtil = new DateKeyboardUtil(this, mKeyboardView, mEditYear, mEditMonth, mEditDay);

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

    private void readExtra() {
        Intent intent = getIntent();
        receiptInfo = (ReceiptInfo) intent.getSerializableExtra("info");
        orderOtherId = intent.getStringExtra("orderId");
        containerId = intent.getStringExtra("containerId");
        barCode = intent.getStringExtra("barCode");
    }

    private void fillData() {
        if (receiptInfo == null) {
            return;
        }
        itemNameTextView.setText(receiptInfo.getSkuName());
        packUnitTextView.setText(receiptInfo.getPackUnit());
        orderQtyTextView.setText(String.valueOf(receiptInfo.getOrderQty()));
        inboundQtyEditView.setHint(String.valueOf(receiptInfo.getOrderQty()));
        inboundQtyEditView.setText(null);

        if ("1".equals(receiptInfo.getBatchNeeded())) {
            lotNumLayout.setVisibility(View.VISIBLE);
        } else {
            lotNumLayout.setVisibility(View.GONE);
        }
    }

    private void pressBack() {
        String inboundQty = inboundQtyEditView.getText().toString();
        String year = mEditYear.getText().toString();
        String month = mEditMonth.getText().toString();
        String day = mEditDay.getText().toString();
        String lotNum = lotNumEditText.getText().toString();

        if (!TextUtils.isEmpty(inboundQty) || !TextUtils.isEmpty(year) || !TextUtils.isEmpty(month) || !TextUtils.isEmpty(day) || !TextUtils.isEmpty(lotNum)) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void submit() {
        if (receiptInfo == null) {
            return;
        }

        String inboundQty = inboundQtyEditView.getValue();
        String year = mEditYear.getText().toString();
        String month = mEditMonth.getText().toString();
        String day = mEditDay.getText().toString();
        String proTime = year + "-" + month + "-" + day;
        String lotNum = lotNumEditText.getText().toString();

        if (TextUtils.isEmpty(inboundQty)) {
            Toast.makeText(this, "请填入收货数量", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
            Toast.makeText(this, "请填入完整的生产日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("1".equals(receiptInfo.getBatchNeeded()) && TextUtils.isEmpty(lotNum)) {
            Toast.makeText(this, "请填入批次号", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barCode", barCode);
            jsonObject.put("proTime", proTime);
            jsonObject.put("lotNum", lotNum);
            jsonObject.put("inboundQty", inboundQty);

            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new ReceiptAddTask(this, orderOtherId, containerId, null, null, jsonArray.toString()).start();
    }

    private class ReceiptAddTask extends HttpAsyncTask<ResponseState> {

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
        String items;


        public ReceiptAddTask(Context context, String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
            super(context, true, true);
            this.orderOtherId = orderOtherId;
            this.bookingNum = bookingNum;
            this.containerId = containerId;
            this.receiptWharf = receiptWharf;
            this.items = items;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ReceiptAddProvider.request(orderOtherId, containerId, bookingNum, receiptWharf, items);
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
