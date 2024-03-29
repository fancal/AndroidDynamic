package com.elianshang.wms.app.receipt.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
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

    private TextView barcodeTextView;

    private TextView skuCodeTextView;

    private View timeLayout;

    private View exceptionCodeLayout;

    /**
     * 商品包装数TextView
     */
    private TextView packUnitTextView;

    private TextView pileTextView;

    /**
     * 订单商品数
     */
    private TextView orderQtyTextView;

    /**
     * 实际数量输入框-箱
     */
    private QtyEditText inboundQtyEditView;
    /**
     * 实际数量输入框-EA
     */
    private QtyEditText scatterQtyEditView;

    /**
     * 批次号布局
     */
    private View lotNumLayout;

    /**
     * 批次号输入框
     */
    private ContentEditText lotNumEditText;

    private CheckBox preDataCheckBox;

    private CheckBox dueDataCheckBox;

    private EditText exceptionCodeTextView;

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
     * 生成日期-年
     */
    private String proYear;
    /**
     * 生成日期-月
     */
    private String proMonth;
    /**
     * 生成日期-日
     */
    private String proDay;
    /**
     * 到期日期-年
     */
    private String dueYear;
    /**
     * 到期日期-月
     */
    private String dueMonth;
    /**
     * 到期日期-日
     */
    private String dueDay;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    private String serialNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity_orderinfo);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        if (readExtra()) {
            findView();
            fillData();
        }
    }

    private void findView() {
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        barcodeTextView = (TextView) findViewById(R.id.barcode_TextView);
        skuCodeTextView = (TextView) findViewById(R.id.skuCode_TextView);
        packUnitTextView = (TextView) findViewById(R.id.packUnit_TextView);
        pileTextView = (TextView) findViewById(R.id.pile_TextView);
        orderQtyTextView = (TextView) findViewById(R.id.orderQty_TextView);
        inboundQtyEditView = (QtyEditText) findViewById(R.id.inboundQty_EditView);
        scatterQtyEditView = (QtyEditText) findViewById(R.id.scatterQty_EditView);
        lotNumLayout = findViewById(R.id.lotNum_Layout);
        lotNumEditText = (QtyEditText) findViewById(R.id.lotNum_EditText);
        preDataCheckBox = (CheckBox) findViewById(R.id.preData_CheckBox);
        dueDataCheckBox = (CheckBox) findViewById(R.id.dueData_CheckBox);
        exceptionCodeLayout = findViewById(R.id.exceptionCode_Layout);
        exceptionCodeTextView = (EditText) findViewById(R.id.exceptionCode_TextView);
        submitButton = (Button) findViewById(R.id.submit_Button);
        timeLayout = findViewById(R.id.time_Layout);
        mEditYear = (EditText) findViewById(R.id.et_year);
        mEditMonth = (EditText) findViewById(R.id.et_month);
        mEditDay = (EditText) findViewById(R.id.et_day);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardTextView = (TextView) findViewById(R.id.keyboard_text);
        keyboardUtil = new DateKeyboardUtil(that, mKeyboardView, mEditYear, mEditMonth, mEditDay);
        keyboardUtil.setOnKeyBoardUtilListener(this);
        submitButton.setOnClickListener(this);
        preDataCheckBox.setOnClickListener(this);
        dueDataCheckBox.setOnClickListener(this);

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
        barcodeTextView.setText(orderReceiptInfo.getBarcode());
        skuCodeTextView.setText(orderReceiptInfo.getSkuCode());
        packUnitTextView.setText(orderReceiptInfo.getPackName());
        pileTextView.setText(orderReceiptInfo.getPile());
        orderQtyTextView.setText(orderReceiptInfo.getOrderQty());
        inboundQtyEditView.setText(null);
        scatterQtyEditView.setText(null);

        if (1 == orderReceiptInfo.getBatchNeeded()) {
            lotNumLayout.setVisibility(View.VISIBLE);
            lotNumEditText.setHint("必填");
        } else {
            lotNumLayout.setVisibility(View.VISIBLE);
            lotNumEditText.setHint("选填");
        }

        if (orderReceiptInfo.getIsNeedProTime() == 1) {
            timeLayout.setVisibility(View.VISIBLE);
            exceptionCodeLayout.setVisibility(View.VISIBLE);
            mEditYear.setFocusable(true);
            mEditMonth.setFocusable(true);
            mEditDay.setFocusable(true);
            exceptionCodeTextView.setFocusable(true);
        } else {
            timeLayout.setVisibility(View.GONE);
            exceptionCodeLayout.setVisibility(View.GONE);
            mEditYear.setFocusable(false);
            mEditMonth.setFocusable(false);
            mEditDay.setFocusable(false);
            exceptionCodeTextView.setFocusable(false);

            scatterQtyEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        lotNumEditText.requestFocus();
//                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(that.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void pressBack() {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return;
        }
        String inboundQty = inboundQtyEditView.getValue();
        String inboundEA = scatterQtyEditView.getValue();
        String year = null;
        String month = null;
        String day = null;
        String lotNum = null;
        Editable yearEditable = mEditYear.getText();
        Editable monthEditable = mEditMonth.getText();
        Editable dayEditable = mEditDay.getText();
        Editable lotNumEditable = lotNumEditText.getText();
        if (yearEditable != null) {
            year = yearEditable.toString();
        }
        if (monthEditable != null) {
            month = monthEditable.toString();
        }
        if (dayEditable != null) {
            day = dayEditable.toString();
        }
        if (lotNumEditable != null) {
            lotNum = lotNumEditable.toString();
        }

        if (!TextUtils.isEmpty(inboundQty) || !TextUtils.isEmpty(inboundEA) || !TextUtils.isEmpty(year) || !TextUtils.isEmpty(month) || !TextUtils.isEmpty(day) || !TextUtils.isEmpty(lotNum)) {
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
        } else if (v == preDataCheckBox) {
            Editable yearEditable = mEditYear.getText();
            Editable monthEditable = mEditMonth.getText();
            Editable dayEditable = mEditDay.getText();
            if (yearEditable != null) {
                dueYear = yearEditable.toString();
            }
            if (monthEditable != null) {
                dueMonth = monthEditable.toString();
            }
            if (dayEditable != null) {
                dueDay = dayEditable.toString();
            }
            mEditYear.setText(proYear);
            mEditMonth.setText(proMonth);
            mEditDay.setText(proDay);

            preDataCheckBox.setChecked(true);
            dueDataCheckBox.setChecked(false);
            keyboardUtil.setYearAsc(false);
        } else if (v == dueDataCheckBox) {
            Editable yearEditable = mEditYear.getText();
            Editable monthEditable = mEditMonth.getText();
            Editable dayEditable = mEditDay.getText();
            if (yearEditable != null) {
                proYear = yearEditable.toString();
            }
            if (monthEditable != null) {
                proMonth = monthEditable.toString();
            }
            if (dayEditable != null) {
                proDay = dayEditable.toString();
            }
            mEditYear.setText(dueYear);
            mEditMonth.setText(dueMonth);
            mEditDay.setText(dueDay);

            dueDataCheckBox.setChecked(true);
            preDataCheckBox.setChecked(false);
            keyboardUtil.setYearAsc(true);

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
        String scatterQty = scatterQtyEditView.getValue();

        if (TextUtils.isEmpty(inboundQty) && TextUtils.isEmpty(scatterQty)) {
            Toast.makeText(that, "请输入正确的数量", Toast.LENGTH_SHORT).show();
            return;
        }

        String lotNum = null;
        Editable lotNumEditable = lotNumEditText.getText();
        if (lotNumEditable != null) {
            lotNum = lotNumEditable.toString();
        }
        if (1 == orderReceiptInfo.getBatchNeeded() && TextUtils.isEmpty(lotNum)) {
            Toast.makeText(that, "请填入批次号", Toast.LENGTH_SHORT).show();
            return;
        }

        String exceptionCode = "";
        String proTime = "";
        String dueTime = "";

        if (orderReceiptInfo.getIsNeedProTime() == 1) {
            String year = null;
            String month = null;
            String day = null;
            Editable yearEditable = mEditYear.getText();
            Editable monthEditable = mEditMonth.getText();
            Editable dayEditable = mEditDay.getText();
            if (yearEditable != null) {
                year = yearEditable.toString();
            }
            if (monthEditable != null) {
                month = monthEditable.toString();
            }
            if (dayEditable != null) {
                day = dayEditable.toString();
            }
            if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
                Toast.makeText(that, "请填入完整的日期", Toast.LENGTH_SHORT).show();
                return;
            }

            if (month.length() == 1) {
                month = "0" + month;
            }

            if (day.length() == 1) {
                day = "0" + day;
            }

            proTime = year + "-" + month + "-" + day;
            exceptionCode = exceptionCodeTextView.getText().toString();

            dueTime = proTime;
            if (preDataCheckBox.isChecked()) {
                dueTime = "";
            } else if (dueDataCheckBox.isChecked()) {
                proTime = "";
            }
        }

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barCode", barCode);
            jsonObject.put("proTime", proTime);
            jsonObject.put("packName", orderReceiptInfo.getPackName());
            jsonObject.put("lotNum", lotNum);
            jsonObject.put("inboundQty", inboundQty);
            jsonObject.put("scatterQty", scatterQty);
            jsonObject.put("dueTime", dueTime);
            jsonObject.put("exceptionCode", exceptionCode);

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
