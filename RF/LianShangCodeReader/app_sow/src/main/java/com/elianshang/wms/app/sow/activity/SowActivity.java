package com.elianshang.wms.app.sow.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.xue.http.impl.DataHull;

public class SowActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, TextWatcher {

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

    private View oneLayout;

    private View oneContainerLayout;

    private ScanEditText oneContainerIdEditText;

    private View oneOrderLayout;

    private ScanEditText oneOrderIdEditText;

    private ScanEditText oneOrderBarcodeEditText;

    private View twoLayout;

    private TextView twoHeadTextView;

    private View twoContainerIdLayout;

    private ScanEditText twoContainerIdEditText;

    private View twoStoreNameLayout;

    private TextView twoStoreNameTextView;

    private View twoItemNameLayout;

    private TextView twoItemNameTextView;

    private View twoPackNameLayout;

    private TextView twoPackNameTextView;

    private View twoSystemQtyLayout;

    private TextView twoAllocQtyTextView;

    private View twoInputQtyLayout;

    private QtyEditText twoInputQtyEditView;

    private CheckBox twoInputQtyCheckView;

    private Button submitButton;

    private ScanEditTextTool scanEditTextTool;

    private Sow curSow;

    private String serialNumber;

    private Type mType;

    /**
     * 播种方式
     */
    public static enum Type {
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
        setContentView(R.layout.activity_sow);
        findViews();

        //Fixme test
        ScanManager.init(this);
        if (readExtra()) {
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

        if (curSow != null) {
            fillStepTwoContainerLayout();
        } else {
            fillStepOne();
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
        if (twoInputQtyEditView != null) {
            twoInputQtyEditView.removeTextChangedListener(this);
        }

    }

    private void findViews() {
        oneLayout = findViewById(R.id.sow_one);
        oneContainerLayout = oneLayout.findViewById(R.id.container);
        oneContainerIdEditText = (ScanEditText) oneLayout.findViewById(R.id.containerId_EditText);
        oneOrderLayout = oneLayout.findViewById(R.id.order);
        oneOrderIdEditText = (ScanEditText) oneLayout.findViewById(R.id.orderId_EditText);
        oneOrderBarcodeEditText = (ScanEditText) oneLayout.findViewById(R.id.barcode_EditText);

        twoLayout = findViewById(R.id.sow_two);
        twoHeadTextView = (TextView) twoLayout.findViewById(R.id.head_TextView);
        twoContainerIdLayout = twoLayout.findViewById(R.id.containerId_Layout);
        twoContainerIdEditText = (ScanEditText) twoLayout.findViewById(R.id.containerId_EditText);
        twoContainerIdEditText.setCode(true);
        twoStoreNameLayout = twoLayout.findViewById(R.id.storeName_Layout);
        twoStoreNameTextView = (TextView) twoLayout.findViewById(R.id.storeName_TextView);
        twoItemNameLayout = twoLayout.findViewById(R.id.itemName_Layout);
        twoItemNameTextView = (TextView) twoLayout.findViewById(R.id.itemName_TextView);
        twoPackNameLayout = twoLayout.findViewById(R.id.packName_Layout);
        twoPackNameTextView = (TextView) twoLayout.findViewById(R.id.packName_TextView);
        twoSystemQtyLayout = twoLayout.findViewById(R.id.systemQty_Layout);
        twoAllocQtyTextView = (TextView) twoLayout.findViewById(R.id.allocQty_TextView);
        twoInputQtyLayout = twoLayout.findViewById(R.id.inputQty_Layout);
        twoInputQtyEditView = (QtyEditText) twoLayout.findViewById(R.id.inputQty_EditView);
        twoInputQtyCheckView = (CheckBox) twoLayout.findViewById(R.id.inputQty_CheckView);

        submitButton = (Button) findViewById(R.id.submit_Button);

        initToolbar();
    }

    private void fillStepOne() {
        oneLayout.setVisibility(View.VISIBLE);
        twoLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        if (mType == Type.CONTAINER) {
            oneContainerLayout.setVisibility(View.VISIBLE);
            oneOrderLayout.setVisibility(View.GONE);
            oneContainerIdEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, oneContainerIdEditText);
            scanEditTextTool.setComplete(this);
        } else {
            oneContainerLayout.setVisibility(View.GONE);
            oneOrderLayout.setVisibility(View.VISIBLE);
            oneOrderIdEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, oneOrderIdEditText, oneOrderBarcodeEditText);
            scanEditTextTool.setComplete(this);
        }

    }

    private void fillStepTwoContainerLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        oneLayout.setVisibility(View.GONE);
        twoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(null);

        twoHeadTextView.setText("扫描播种托盘码");

        twoContainerIdEditText.setText(null);

        twoContainerIdLayout.setVisibility(View.VISIBLE);
        twoStoreNameLayout.setVisibility(View.GONE);
        twoItemNameLayout.setVisibility(View.GONE);
        twoPackNameLayout.setVisibility(View.GONE);
        twoSystemQtyLayout.setVisibility(View.GONE);
        twoInputQtyLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        twoContainerIdEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, twoContainerIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStepTwoQtyLayout() {
        oneLayout.setVisibility(View.GONE);
        twoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        submitButton.setOnClickListener(this);

        twoHeadTextView.setText("确认播种数量");

        twoStoreNameTextView.setText(curSow.getStoreName());
        twoItemNameTextView.setText(curSow.getSkuName());
        twoPackNameTextView.setText(curSow.getPackName());
        twoAllocQtyTextView.setText(curSow.getQty());
        twoInputQtyEditView.setHint(curSow.getQty());
        twoInputQtyEditView.setText(null);
        twoInputQtyEditView.addTextChangedListener(this);

        twoInputQtyEditView.requestFocus();

        twoContainerIdLayout.setVisibility(View.GONE);
        twoStoreNameLayout.setVisibility(View.VISIBLE);
        twoItemNameLayout.setVisibility(View.VISIBLE);
        twoPackNameLayout.setVisibility(View.VISIBLE);
        twoSystemQtyLayout.setVisibility(View.VISIBLE);
        twoInputQtyLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void fillStepThree() {
        fillStepOne();
        curSow = null;
        oneContainerIdEditText.setText(null);
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

    @Override
    public void onComplete() {
        if (oneLayout.getVisibility() == View.VISIBLE) {
            if (mType == Type.CONTAINER) {
                Editable editable = oneContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
                        new AssignByContainerTask(that, uId, containerId).start();
                    }
                }
            } else {
                Editable editable1 = oneOrderIdEditText.getText();
                Editable editable2 = oneOrderBarcodeEditText.getText();
                if (editable1 != null && editable2 != null) {
                    String orderId = editable1.toString();
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(barcode)) {
                        new AssignByOrderTask(that, uId, orderId, barcode).start();
                    }
                }
            }

        } else if (twoLayout.getVisibility() == View.VISIBLE) {
            Editable editable = twoContainerIdEditText.getText();
            if (editable != null) {
                fillStepTwoQtyLayout();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            float realQty = Float.parseFloat(twoInputQtyEditView.getValue());
            float qty = Float.parseFloat(curSow.getQty());
            twoInputQtyCheckView.setVisibility(qty > realQty ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (twoLayout.getVisibility() == View.VISIBLE) {
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
        if (v == submitButton) {
            String taskId = curSow.getTaskId();
            String containerId = twoContainerIdEditText.getText().toString();
            String qty = twoInputQtyEditView.getValue();
            String type = "";
            if (twoInputQtyCheckView.getVisibility() == View.VISIBLE) {
                type = twoInputQtyCheckView.isChecked() ? "1" : "2";
            }

            new ScanTargetContainerTask(that, uId, taskId, containerId, qty, type).start();
        }
    }

    private class AssignByContainerTask extends HttpAsyncTask<Sow> {

        private String uId;

        private String containerId;

        public AssignByContainerTask(Context context, String uId, String containerId) {
            super(context, true, true, false);
            this.uId = uId;
            this.containerId = containerId;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return AssignByContainerIdProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillStepTwoContainerLayout();
        }
    }

    private class AssignByOrderTask extends HttpAsyncTask<Sow> {

        private String uId;

        private String orderId;

        private String barcode;

        public AssignByOrderTask(Context context, String uId, String orderId, String barcode) {
            super(context, true, true, false);
            this.uId = uId;
            this.orderId = orderId;
            this.barcode = barcode;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return AssignByOrderIdProvider.request(context, uId, uToken, orderId, barcode);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillStepTwoContainerLayout();
        }
    }

    private class ScanTargetContainerTask extends HttpAsyncTask<SowNext> {

        private String uId;
        private String taskId;
        private String containerId;
        private String qty;
        private String type;

        public ScanTargetContainerTask(Context context, String uId, String taskId, String containerId, String qty, String type) {
            super(context, true, true, false);
            this.uId = uId;
            this.taskId = taskId;
            this.qty = qty;
            this.containerId = containerId;
            this.type = type;
        }

        @Override
        public DataHull<SowNext> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, taskId, containerId, qty, type, serialNumber);
        }

        @Override
        public void onPostExecute(SowNext result) {
            if (result.isDone()) {
                fillStepThree();
            } else {
                curSow = result.getSow();
                fillStepTwoContainerLayout();
            }
        }
    }
}
