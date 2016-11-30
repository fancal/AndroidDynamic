package com.elianshang.wms.app.sow.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.sow.R;
import com.elianshang.wms.app.sow.bean.Sow;
import com.elianshang.wms.app.sow.bean.SowNext;
import com.elianshang.wms.app.sow.provider.AssignByContainerIdProvider;
import com.elianshang.wms.app.sow.provider.AssignByOrderIdProvider;
import com.elianshang.wms.app.sow.provider.ScanContainerProvider;
import com.elianshang.wms.app.sow.provider.ViewProvider;
import com.xue.http.impl.DataHull;

public class SowActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Sow sow, Type type) {
        DLIntent intent = new DLIntent(activity.getPackageName(), SowActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (type != null) {
            intent.putExtra("type", type.intValue());
        }
        if (sow != null) {
            intent.putExtra("sow", sow);
        }
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private View scanLayout;

    private View scanContainerLayout;

    private ScanEditText scanContainerIdEditText;

    private View scanOrderLayout;

    private ScanEditText scanOrderIdEditText;

    private ScanEditText scanOrderBarcodeEditText;

    private Button scanSysSubmitButton;

    private Button scanMeSubmitButton;

    private View waitLayout;

    private ScanEditText waitStoreEditText;

    private View detailLayout;

    private TextView detailHeadTextView;

    private ScanEditText detailContainerIdEditText;

    private TextView detailStoreNameTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailAllocQtyTextView;

    private QtyEditText detailInputScatterQtyEditView;

    private QtyEditText detailInputQtyEditView;

    private EditText detailExceptionCodeEditView;

    private Button detailGoOnSubmitButton;

    private Button detailSkipSubmitButton;

    private Button detailStopSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private Sow curSow;

    private String serialNumber;

    private Type mType;

    /**
     * 播种方式
     */
    public enum Type {
        /**
         * 托盘码播种
         */
        CONTAINER(1),

        /**
         * 订单播种
         */
        ORDER(2);

        int type;

        Type(int type) {
            this.type = type;
        }

        public int intValue() {
            return type;
        }

        public static Type getType(int value) {
            if (value == 2) {
                return ORDER;
            }
            return CONTAINER;
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sow_activity_main);

        if (readExtra()) {
            findViews();
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        curSow = (Sow) intent.getSerializableExtra("sow");
        mType = Type.getType(intent.getIntExtra("type", 1));

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
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void findViews() {
        scanLayout = findViewById(R.id.sow_scan);
        scanContainerLayout = scanLayout.findViewById(R.id.container);
        scanContainerIdEditText = (ScanEditText) scanLayout.findViewById(R.id.containerId_EditText);
        scanOrderLayout = scanLayout.findViewById(R.id.order);
        scanOrderIdEditText = (ScanEditText) scanLayout.findViewById(R.id.orderId_EditText);
        scanOrderBarcodeEditText = (ScanEditText) scanLayout.findViewById(R.id.barcode_EditText);
        scanSysSubmitButton = (Button) scanLayout.findViewById(R.id.sysSubmit_Button);
        scanMeSubmitButton = (Button) scanLayout.findViewById(R.id.meSubmit_Button);

        waitLayout = findViewById(R.id.sow_wait);

        waitStoreEditText = (ScanEditText) waitLayout.findViewById(R.id.store_EditText);

        detailLayout = findViewById(R.id.sow_detail);
        detailHeadTextView = (TextView) detailLayout.findViewById(R.id.head_TextView);
        detailContainerIdEditText = (ScanEditText) detailLayout.findViewById(R.id.containerId_EditText);
        detailContainerIdEditText.setCode(true);
        detailStoreNameTextView = (TextView) detailLayout.findViewById(R.id.storeName_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailAllocQtyTextView = (TextView) detailLayout.findViewById(R.id.allocQty_TextView);
        detailInputQtyEditView = (QtyEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailInputScatterQtyEditView = (QtyEditText) detailLayout.findViewById(R.id.inputScatterQty_EditView);
        detailExceptionCodeEditView = (EditText) detailLayout.findViewById(R.id.exception_EditView);
        detailGoOnSubmitButton = (Button) detailLayout.findViewById(R.id.goonsubmit_Button);
        detailSkipSubmitButton = (Button) detailLayout.findViewById(R.id.skipsubmit_Button);
        detailStopSubmitButton = (Button) detailLayout.findViewById(R.id.stopsubmit_Button);

        initToolbar();

        if (curSow != null) {
            fillDetail();
        } else {
            fillScan();
        }
    }

    private void fillScan() {
        scanLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.GONE);

        scanSysSubmitButton.setOnClickListener(this);
        scanMeSubmitButton.setOnClickListener(this);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        if (mType == Type.CONTAINER) {
            scanContainerLayout.setVisibility(View.VISIBLE);
            scanOrderLayout.setVisibility(View.GONE);
            scanContainerIdEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, scanContainerIdEditText);
            scanEditTextTool.setComplete(this);
        } else {
            scanContainerLayout.setVisibility(View.GONE);
            scanOrderLayout.setVisibility(View.VISIBLE);
            scanOrderIdEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, scanOrderIdEditText, scanOrderBarcodeEditText);
            scanEditTextTool.setComplete(this);
        }
    }

    private void fillWait() {
        scanLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        waitStoreEditText.setText("");
        waitStoreEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, waitStoreEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillDetail() {
        if (curSow == null) {
            return;
        }
        if (!TextUtils.isEmpty(curSow.getTaskId()) && TextUtils.isEmpty(curSow.getStoreName()) && TextUtils.isEmpty(curSow.getQty())) {
            fillWait();
            return;
        }

        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
        waitLayout.setVisibility(View.GONE);
        detailGoOnSubmitButton.setVisibility(View.VISIBLE);
        detailSkipSubmitButton.setVisibility(View.VISIBLE);
        detailStopSubmitButton.setVisibility(View.VISIBLE);
        detailGoOnSubmitButton.setEnabled(false);
        detailSkipSubmitButton.setEnabled(false);
        detailStopSubmitButton.setEnabled(false);

        detailGoOnSubmitButton.setOnClickListener(this);
        detailSkipSubmitButton.setOnClickListener(this);
        detailStopSubmitButton.setOnClickListener(this);

        detailHeadTextView.setText("确认播种数量");
        detailExceptionCodeEditView.setText("");

        detailStoreNameTextView.setText(curSow.getStoreName());
        detailItemNameTextView.setText(curSow.getSkuName());
        detailPackNameTextView.setText(curSow.getPackName());
        detailAllocQtyTextView.setText(curSow.getQty());
        detailInputQtyEditView.setHint("0");
        detailInputScatterQtyEditView.setHint("0");
        detailInputScatterQtyEditView.setText(null);
        detailInputQtyEditView.setText(null);

        detailInputQtyEditView.requestFocus();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        detailContainerIdEditText.requestFocus();
        detailContainerIdEditText.setText(null);
        scanEditTextTool = new ScanEditTextTool(that, detailContainerIdEditText);
        scanEditTextTool.setComplete(this);


    }

    private void fillComplete() {
        fillScan();
        curSow = null;
        scanContainerIdEditText.setText(null);
        scanOrderIdEditText.setText(null);
        scanOrderBarcodeEditText.setText(null);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(mType == Type.CONTAINER ? "托盘播种" : "订单播种");
    }

    @Override
    public void onComplete() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            if (mType == Type.CONTAINER) {
                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
                        scanSysSubmitButton.setEnabled(true);
                        scanMeSubmitButton.setEnabled(true);
                    } else {
                        scanSysSubmitButton.setEnabled(false);
                        scanMeSubmitButton.setEnabled(false);
                    }
                }
            } else {
                Editable editable1 = scanOrderIdEditText.getText();
                Editable editable2 = scanOrderBarcodeEditText.getText();
                if (editable1 != null && editable2 != null) {
                    String orderId = editable1.toString();
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(barcode)) {
                        scanSysSubmitButton.setEnabled(true);
                        scanMeSubmitButton.setEnabled(true);
                    } else {
                        scanSysSubmitButton.setEnabled(false);
                        scanMeSubmitButton.setEnabled(false);
                    }
                }
            }
        } else if (waitLayout.getVisibility() == View.VISIBLE) {
            Editable editable = waitStoreEditText.getText();
            if (editable != null) {
                String storeId = editable.toString();
                new ViewTask(that, curSow.getTaskId(), storeId).start();
            }

        } else if (detailLayout.getVisibility() == View.VISIBLE) {
            Editable editable = detailContainerIdEditText.getText();
            if (editable != null) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    detailGoOnSubmitButton.setEnabled(true);
                    detailSkipSubmitButton.setEnabled(true);
                    detailStopSubmitButton.setEnabled(true);
                    detailInputQtyEditView.requestFocus();
                }
            }
        }
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onBackPressed() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
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
    public void onClick(View v) {
        if (v == scanSysSubmitButton) {
            if (mType == Type.CONTAINER) {
                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
                        new AssignByContainerTask(that, containerId, "0").start();
                    }
                }
            } else {
                Editable editable1 = scanOrderIdEditText.getText();
                Editable editable2 = scanOrderBarcodeEditText.getText();
                if (editable1 != null && editable2 != null) {
                    String orderId = editable1.toString();
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(barcode)) {
                        new AssignByOrderTask(that, orderId, barcode, "0").start();
                    }
                }
            }
        } else if (v == scanMeSubmitButton) {
            if (mType == Type.CONTAINER) {
                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
                        new AssignByContainerTask(that, containerId, "1").start();
                    }
                }
            } else {
                Editable editable1 = scanOrderIdEditText.getText();
                Editable editable2 = scanOrderBarcodeEditText.getText();
                if (editable1 != null && editable2 != null) {
                    String orderId = editable1.toString();
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(barcode)) {
                        new AssignByOrderTask(that, orderId, barcode, "1").start();
                    }
                }
            }
        } else if (v == detailGoOnSubmitButton) {
            String taskId = curSow.getTaskId();
            String containerId = detailContainerIdEditText.getText().toString();
            String qty = detailInputQtyEditView.getValue();
            String scatterQty = detailInputScatterQtyEditView.getValue();
            String type = "2";
            String exceptionCode = detailExceptionCodeEditView.getText().toString();

            new ScanTargetContainerTask(that, taskId, containerId, qty, scatterQty, type, curSow.getStoreNo(), exceptionCode).start();
        } else if (v == detailSkipSubmitButton) {
            String taskId = curSow.getTaskId();
            String containerId = detailContainerIdEditText.getText().toString();
            String qty = detailInputQtyEditView.getValue();
            String scatterQty = detailInputScatterQtyEditView.getValue();
            String type = "3";
            String exceptionCode = detailExceptionCodeEditView.getText().toString();


            new ScanTargetContainerTask(that, taskId, containerId, qty, scatterQty, type, curSow.getStoreNo(), exceptionCode).start();
        } else if (v == detailStopSubmitButton) {
            String taskId = curSow.getTaskId();
            String containerId = detailContainerIdEditText.getText().toString();
            String qty = detailInputQtyEditView.getValue();
            String scatterQty = detailInputScatterQtyEditView.getValue();
            String type = "1";
            String exceptionCode = detailExceptionCodeEditView.getText().toString();

            new ScanTargetContainerTask(that, taskId, containerId, qty, scatterQty, type, curSow.getStoreNo(), exceptionCode).start();
        }
    }

    private class AssignByContainerTask extends HttpAsyncTask<Sow> {

        private String containerId;

        private String type;

        public AssignByContainerTask(Context context, String containerId, String type) {
            super(context, true, true, false);
            this.containerId = containerId;
            this.type = type;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return AssignByContainerIdProvider.request(context, uId, uToken, containerId, type);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillDetail();
        }
    }

    private class ViewTask extends HttpAsyncTask<Sow> {

        private String storeId;

        private String taskId;

        public ViewTask(Context context, String taskId, String storeId) {
            super(context, true, true, false);
            this.storeId = storeId;
            this.taskId = taskId;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return ViewProvider.request(context, uId, uToken, taskId, storeId);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillDetail();
        }
    }

    private class AssignByOrderTask extends HttpAsyncTask<Sow> {

        private String orderId;

        private String barcode;

        private String type;

        public AssignByOrderTask(Context context, String orderId, String barcode, String type) {
            super(context, true, true, false);
            this.orderId = orderId;
            this.barcode = barcode;
            this.type = type;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return AssignByOrderIdProvider.request(context, uId, uToken, orderId, barcode, type);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillDetail();
        }
    }

    private class ScanTargetContainerTask extends HttpAsyncTask<SowNext> {

        private String taskId;
        private String containerId;
        private String qty;
        private String scatterQty;
        private String type;
        private String storeNo;
        private String exceptionCode;

        public ScanTargetContainerTask(Context context, String taskId, String containerId, String qty, String scatterQty, String type, String storeNo, String exceptionCode) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.qty = qty;
            this.containerId = containerId;
            this.type = type;
            this.exceptionCode = exceptionCode;
            this.storeNo = storeNo;
            this.scatterQty = scatterQty;
        }

        @Override
        public DataHull<SowNext> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, taskId, containerId, qty, scatterQty, type, storeNo, exceptionCode, serialNumber);
        }

        @Override
        public void onPostExecute(SowNext result) {
            if (result.isDone()) {
                fillComplete();
            } else {
                curSow = result.getSow();
                fillDetail();
            }
        }
    }
}
