package com.elianshang.wms.app.sow.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.elianshang.wms.app.sow.bean.StoreList;
import com.elianshang.wms.app.sow.provider.ScanContainerProvider;
import com.xue.http.impl.DataHull;

public class SowDetailActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Sow sow, String title) {
        DLIntent intent = new DLIntent(activity.getPackageName(), SowDetailActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (sow != null) {
            intent.putExtra("sow", sow);
        }
        intent.putExtra("title", title);
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

    private String title;

    private Toolbar mToolbar;

    private View detailLayout;

    private TextView detailHeadTextView;

    private ScanEditText detailContainerIdEditText;

    private TextView detailStoreNameTextView;

    private TextView detailStoreNoTextView;

    private TextView detailItemNameTextView;

    private TextView detailBarcodeTextView;

    private TextView detailSkuCodeTextView;

    private TextView detailPackNameTextView;

    private TextView detailAllocQtyTextView;

    private QtyEditText detailInputScatterQtyEditView;

    private QtyEditText detailInputQtyEditView;

    private EditText detailExceptionCodeEditView;

    private Button detailGoOnSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private Sow curSow;

    private String serialNumber;

    private boolean isItemClick = false ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sow_activity_detail);

        if (readExtra()) {
            findViews();
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        title = intent.getStringExtra("title");
        curSow = (Sow) intent.getSerializableExtra("sow");

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

        detailLayout = findViewById(R.id.sow_detail);
        detailHeadTextView = (TextView) detailLayout.findViewById(R.id.head_TextView);
        detailContainerIdEditText = (ScanEditText) detailLayout.findViewById(R.id.containerId_EditText);
        detailContainerIdEditText.setCode(true);
        detailStoreNameTextView = (TextView) detailLayout.findViewById(R.id.storeName_TextView);
        detailStoreNoTextView = (TextView) detailLayout.findViewById(R.id.storeNo_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailBarcodeTextView = (TextView) detailLayout.findViewById(R.id.barcode_TextView);
        detailSkuCodeTextView = (TextView) detailLayout.findViewById(R.id.skuCode_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailAllocQtyTextView = (TextView) detailLayout.findViewById(R.id.allocQty_TextView);
        detailInputQtyEditView = (QtyEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailInputScatterQtyEditView = (QtyEditText) detailLayout.findViewById(R.id.inputScatterQty_EditView);
        detailExceptionCodeEditView = (EditText) detailLayout.findViewById(R.id.exception_EditView);
        detailGoOnSubmitButton = (Button) detailLayout.findViewById(R.id.goonsubmit_Button);

        initToolbar();

        if (curSow != null) {
            fillDetail();
        } else {
            finish();
        }
    }

    private void fillDetail() {
        if (curSow == null) {
            return;
        }


        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        detailLayout.setVisibility(View.VISIBLE);
        detailGoOnSubmitButton.setVisibility(View.VISIBLE);
        detailGoOnSubmitButton.setEnabled(false);

        detailGoOnSubmitButton.setOnClickListener(this);

        detailHeadTextView.setText("确认播种数量");
        detailExceptionCodeEditView.setText("");

        detailStoreNameTextView.setText(curSow.getCustomerName());
        detailStoreNoTextView.setText(curSow.getCustomerCode());
        detailItemNameTextView.setText(curSow.getSkuName());
        detailBarcodeTextView.setText(curSow.getBarcode());
        detailSkuCodeTextView.setText(curSow.getSkuCode());
        detailPackNameTextView.setText(curSow.getPackName());
        detailAllocQtyTextView.setText(curSow.getQty());
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


    private void fillComplete(StoreList storeList) {
        Intent intent = new Intent();
        if (storeList != null) {
            intent.putExtra("storeList", storeList);
        }
        that.setResult(RESULT_OK, intent);
        finish();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(title);
    }

    @Override
    public void onComplete() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            Editable editable = detailContainerIdEditText.getText();
            if (editable != null) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    detailGoOnSubmitButton.setEnabled(true);
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
        if (isItemClick) {
            return;
        }

        isItemClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isItemClick = false;
            }
        }, 500);

        if (v == detailGoOnSubmitButton) {
            String taskId = curSow.getTaskId();
            String containerId = detailContainerIdEditText.getText().toString();
            String qty = detailInputQtyEditView.getValue();
            String scatterQty = detailInputScatterQtyEditView.getValue();
            String type = "2";
            String exceptionCode = detailExceptionCodeEditView.getText().toString();

            if (TextUtils.isEmpty(qty) && TextUtils.isEmpty(scatterQty)) {
                Toast.makeText(that, "请输入正确的数量", Toast.LENGTH_SHORT).show();
                return;
            }

            new ScanTargetContainerTask(that, taskId, containerId, qty, scatterQty, type, curSow.getCustomerCode(), exceptionCode).start();
        }
    }

    private class ScanTargetContainerTask extends HttpAsyncTask<StoreList> {

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
        public DataHull<StoreList> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, taskId, containerId, qty, scatterQty, type, storeNo, exceptionCode, serialNumber);
        }

        @Override
        public void onPostExecute(StoreList result) {
            fillComplete(result);
        }

        @Override
        public void dataNull(String errMsg) {
            fillComplete(null);
        }
    }

}
