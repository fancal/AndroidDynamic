package com.elianshang.wms.app.receipt.activity;

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
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.receipt.R;
import com.elianshang.wms.app.receipt.bean.OrderReceiptInfo;
import com.elianshang.wms.app.receipt.bean.ResponseState;
import com.elianshang.wms.app.receipt.provider.AddProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 收货详情完成页
 */
public class OrderInfoActivity extends DLBasePluginActivity implements View.OnClickListener, DateKeyboardUtil.OnKeyBoardUtilListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken, String orderOtherId, String containerId, String barCode, OrderReceiptInfo orderReceiptInfo) {
        DLIntent intent = new DLIntent(activity.getPackageName(), OrderInfoActivity.class);
        intent.putExtra("orderOtherId", orderOtherId);
        intent.putExtra("containerId", containerId);
        intent.putExtra("barCode", barCode);
        intent.putExtra("orderReceiptInfo", orderReceiptInfo);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

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
    private OrderReceiptInfo orderReceiptInfo;

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

    private TextView mKeyboardTextView;

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

    private String serialNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderreceiptinfo);

        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        if (readExtra()) {
            findView();
            fillData();
        }
    }

    private void findView() {
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        packUnitTextView = (TextView) findViewById(R.id.packUnit_TextView);
        orderQtyTextView = (TextView) findViewById(R.id.orderQty_TextView);
        inboundQtyEditView = (QtyEditText) findViewById(R.id.inboundQty_EditView);
        lotNumLayout = findViewById(R.id.lotNum_Layout);
        lotNumEditText = (QtyEditText) findViewById(R.id.lotNum_EditText);
        submitButton = (Button) findViewById(R.id.submit_Button);
        mEditYear = (EditText) findViewById(R.id.et_year);
        mEditMonth = (EditText) findViewById(R.id.et_month);
        mEditDay = (EditText) findViewById(R.id.et_day);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardTextView = (TextView) findViewById(R.id.keyboard_text);
        keyboardUtil = new DateKeyboardUtil(that, mKeyboardView, mEditYear, mEditMonth, mEditDay);
        keyboardUtil.setOnKeyBoardUtilListener(this);
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

        orderReceiptInfo = (OrderReceiptInfo) intent.getSerializableExtra("orderReceiptInfo");
        orderOtherId = intent.getStringExtra("orderOtherId");
        containerId = intent.getStringExtra("containerId");
        barCode = intent.getStringExtra("barCode");

        return true;
    }

    private void fillData() {
        if (orderReceiptInfo == null) {
            return;
        }
        itemNameTextView.setText(orderReceiptInfo.getSkuName());
        packUnitTextView.setText(orderReceiptInfo.getPackName());
        orderQtyTextView.setText(orderReceiptInfo.getOrderQty());
        inboundQtyEditView.setHint(orderReceiptInfo.getOrderQty());
        inboundQtyEditView.setText(null);

        if (1 == orderReceiptInfo.getBatchNeeded()) {
            lotNumLayout.setVisibility(View.VISIBLE);
            lotNumEditText.setHint("必填");
        } else {
            lotNumLayout.setVisibility(View.VISIBLE);
            lotNumEditText.setHint("选填");
        }
    }

    private void pressBack() {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return;
        }
        String inboundQty = inboundQtyEditView.getValue();
        String year = mEditYear.getText().toString();
        String month = mEditMonth.getText().toString();
        String day = mEditDay.getText().toString();
        String lotNum = lotNumEditText.getText().toString();

        if (!TextUtils.isEmpty(inboundQty) || !TextUtils.isEmpty(year) || !TextUtils.isEmpty(month) || !TextUtils.isEmpty(day) || !TextUtils.isEmpty(lotNum)) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void submit() {
        if (orderReceiptInfo == null) {
            return;
        }

        String inboundQty = inboundQtyEditView.getValue();

        if (TextUtils.isEmpty(inboundQty)) {
            Toast.makeText(that, "请填入收货数量", Toast.LENGTH_SHORT).show();
            return;
        }

        String year = mEditYear.getText().toString();
        String month = mEditMonth.getText().toString();
        String day = mEditDay.getText().toString();

        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
            Toast.makeText(that, "请填入完整的生产日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (month.length() == 1) {
            month = "0" + month;
        }

        if (day.length() == 1) {
            day = "0" + day;
        }

        String proTime = year + "-" + month + "-" + day;
        String lotNum = lotNumEditText.getText().toString();
        if (1 == orderReceiptInfo.getBatchNeeded() && TextUtils.isEmpty(lotNum)) {
            Toast.makeText(that, "请填入批次号", Toast.LENGTH_SHORT).show();
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

        new ReceiptAddTask(that, uId, uToken, orderOtherId, containerId, null, null, jsonArray.toString()).start();
    }

    @Override
    public void onShow() {
        mKeyboardTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHide() {
        mKeyboardTextView.setVisibility(View.GONE);
    }

    @Override
    public void onTextChange(String year, String mouth, String day) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(year)) {
            sb.append(year + '年');
        }
        if (!TextUtils.isEmpty(mouth)) {
            sb.append(mouth + "月");
        }
        if (!TextUtils.isEmpty(day)) {
            sb.append(day + "日");
        }
        mKeyboardTextView.setText(sb.toString());
    }

    private class ReceiptAddTask extends HttpAsyncTask<ResponseState> {

        String uid;

        String uToken;
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

        public ReceiptAddTask(Context context, String uid, String uToken, String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
            super(context, true, true);
            this.uid = uid;
            this.uToken = uToken;
            this.orderOtherId = orderOtherId;
            this.bookingNum = bookingNum;
            this.containerId = containerId;
            this.receiptWharf = receiptWharf;
            this.items = items;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return AddProvider.request(context, uId, uToken, null, orderOtherId, containerId, bookingNum, receiptWharf, items, serialNumber);
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
