package com.elianshang.wms.app.report.activity;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DateKeyboardUtil;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.report.R;
import com.elianshang.wms.app.report.bean.ResponseState;
import com.elianshang.wms.app.report.provider.OverLossProvider;
import com.xue.http.impl.DataHull;

/**
 * 盘点页面
 */
public class ReportActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, DateKeyboardUtil.OnKeyBoardUtilListener, ScanEditTextTool.OnStateChangeListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private TextView titleTextView;

    private CheckBox lossCheckBox;

    private CheckBox overCheckBox;

    private ScanEditText locationCodeEditText;

    private ScanEditText barcodeEditText;

    private EditText ownerEditText;

    private QtyEditText umoQtyEditView;

    private QtyEditText scatterQtyEditView;

    private Button submitButton;

    /**
     * 键盘工具
     */
    private DateKeyboardUtil keyboardUtil;

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

    private String serialNumber;

    private String type;

    private ScanEditTextTool scanEditTextTool;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);

        if (readExtra()) {
            findView();
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
    public void onDestroy() {
        super.onDestroy();
    }

    private void findView() {
        initToolbar();

        titleTextView = (TextView) findViewById(R.id.title_TextView);
        lossCheckBox = (CheckBox) findViewById(R.id.loss_CheckBox);
        overCheckBox = (CheckBox) findViewById(R.id.over_CheckBox);
        locationCodeEditText = (ScanEditText) findViewById(R.id.locationCode_EditText);
        locationCodeEditText.setCode(true);
        barcodeEditText = (ScanEditText) findViewById(R.id.barcode_EditText);
        ownerEditText = (EditText) findViewById(R.id.owner_EditText);
        umoQtyEditView = (QtyEditText) findViewById(R.id.umoQty_EditView);
        scatterQtyEditView = (QtyEditText) findViewById(R.id.scatterQty_EditView);
        submitButton = (Button) findViewById(R.id.submit_Button);

        mEditYear = (EditText) findViewById(R.id.et_year);
        mEditMonth = (EditText) findViewById(R.id.et_month);
        mEditDay = (EditText) findViewById(R.id.et_day);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardTextView = (TextView) findViewById(R.id.keyboard_text);
        keyboardUtil = new DateKeyboardUtil(that, mKeyboardView, mEditYear, mEditMonth, mEditDay);
        keyboardUtil.setOnKeyBoardUtilListener(this);

        lossCheckBox.setChecked(false);
        overCheckBox.setChecked(false);
        titleTextView.setText("选择操作类型");
        lossCheckBox.setOnClickListener(this);
        overCheckBox.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText, barcodeEditText);
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

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return;
        }

        finish();
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (lossCheckBox == v) {
            lossCheckBox.setChecked(true);
            overCheckBox.setChecked(false);
            titleTextView.setText("报损操作");
            type = "551";
            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
        } else if (overCheckBox == v) {
            overCheckBox.setChecked(true);
            lossCheckBox.setChecked(false);
            titleTextView.setText("报溢操作");
            type = "552";
            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
        } else if (submitButton == v) {
            if (TextUtils.isEmpty(type)) {
                ToastTool.show(that, "请选择上报类型");
                return;
            }

            String locationCode = locationCodeEditText.getText().toString();
            if (TextUtils.isEmpty(locationCode)) {
                ToastTool.show(that, "请扫描库位");
                return;
            }

            String barcode = barcodeEditText.getText().toString();
            if (TextUtils.isEmpty(barcode)) {
                ToastTool.show(that, "请扫描国条码");
                return;
            }

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
            String dueTime = year + month + day;

            String ownerId = ownerEditText.getText().toString();
            if (TextUtils.isEmpty(barcode)) {
                ToastTool.show(that, "请填写货主");
                return;
            }

            String umoQty = umoQtyEditView.getValue();
            String scatterQty = scatterQtyEditView.getValue();

            if (TextUtils.isEmpty(umoQty) && TextUtils.isEmpty(scatterQty)) {
                Toast.makeText(that, "请输入正确的数量", Toast.LENGTH_SHORT).show();
                return;
            }

            new ReportTask(that, type, locationCode, barcode, ownerId, dueTime, umoQty, scatterQty).start();
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

    @Override
    public void onComplete() {
        mEditYear.requestFocus();
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    private class ReportTask extends HttpAsyncTask<ResponseState> {

        private String type;

        private String locationCode;

        private String barcode;

        private String ownerId;

        private String dueTime;

        private String umoQty;

        private String scatterQty;

        private ReportTask(Context context, String type, String locationCode, String barcode, String ownerId, String dueTime, String umoQty, String scatterQty) {
            super(context, true, true, false);
            this.type = type;
            this.locationCode = locationCode;
            this.barcode = barcode;
            this.ownerId = ownerId;
            this.dueTime = dueTime;
            this.umoQty = umoQty;
            this.scatterQty = scatterQty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return OverLossProvider.request(context, uId, uToken, type, locationCode, barcode, ownerId, dueTime, umoQty, scatterQty, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            ToastTool.show(context, "上报成功");
            finish();
        }
    }
}