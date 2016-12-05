package com.elianshang.wms.app.atticshelve.activity;

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
import com.elianshang.wms.app.atticshelve.R;
import com.elianshang.wms.app.atticshelve.bean.AtticShelve;
import com.elianshang.wms.app.atticshelve.bean.AtticShelveNext;
import com.elianshang.wms.app.atticshelve.provider.ScanContainerProvider;
import com.elianshang.wms.app.atticshelve.provider.ScanTargetLocationProvider;
import com.xue.http.impl.DataHull;

public class AtticShelveActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, AtticShelve atticShelve) {
        DLIntent intent = new DLIntent(activity.getPackageName(), AtticShelveActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (atticShelve != null) {
            intent.putExtra("atticShelve", atticShelve);
        }
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private View atticShelveOneLayout;

    private ScanEditText oneContainerIdEditText;

    private View atticShelveTwoLayout;

    private TextView twoHeadTextView;

    private View twoLocationCodeLayout;

    private TextView twoLocationCodeTextView;

    private ScanEditText twoLocationCodeEditText;

    private View twoItemNameLayout;

    private TextView twoItemNameTextView;

    private View twoBarcodeLayout;

    private TextView twoBarcodeTextView;

    private View twoSkuCodeLayout;

    private TextView twoSkuCodeTextView;

    private View twoPackNameLayout;

    private TextView twoPackNameTextView;

    private View twoSystemQtyLayout;

    private TextView twoAllocQtyTextView;

    private View twoInputQtyLayout;

    private QtyEditText twoInputQtyEditView;

    private Button submitButton;

    private ScanEditTextTool scanEditTextTool;

    private AtticShelve curAtticShelve;

    private String serialNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atticshelve_activity_main);
        findViews();

        if (readExtra()) {
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        curAtticShelve = (AtticShelve) intent.getSerializableExtra("atticShelve");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        if (curAtticShelve != null) {
            fillStepTwoLocationLayout();
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
    }

    private void findViews() {
        atticShelveOneLayout = findViewById(R.id.atticshelve_one);
        oneContainerIdEditText = (ScanEditText) atticShelveOneLayout.findViewById(R.id.containerId_EditText);

        atticShelveTwoLayout = findViewById(R.id.atticshelve_two);
        twoHeadTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.head_TextView);
        twoLocationCodeLayout = atticShelveTwoLayout.findViewById(R.id.locationCode_Layout);
        twoLocationCodeTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.locationCode_TextView);
        twoLocationCodeEditText = (ScanEditText) atticShelveTwoLayout.findViewById(R.id.locationCode_EditText);
        twoLocationCodeEditText.setCode(true);
        twoItemNameLayout = atticShelveTwoLayout.findViewById(R.id.itemName_Layout);
        twoItemNameTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.itemName_TextView);
        twoBarcodeLayout = atticShelveTwoLayout.findViewById(R.id.barcode_Layout);
        twoBarcodeTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.barcode_TextView);
        twoSkuCodeLayout = atticShelveTwoLayout.findViewById(R.id.skuCode_Layout);
        twoSkuCodeTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.skuCode_TextView);
        twoPackNameLayout = atticShelveTwoLayout.findViewById(R.id.packName_Layout);
        twoPackNameTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.packName_TextView);
        twoSystemQtyLayout = atticShelveTwoLayout.findViewById(R.id.systemQty_Layout);
        twoAllocQtyTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.allocQty_TextView);
        twoInputQtyLayout = atticShelveTwoLayout.findViewById(R.id.inputQty_Layout);
        twoInputQtyEditView = (QtyEditText) atticShelveTwoLayout.findViewById(R.id.inputQty_EditView);

        submitButton = (Button) findViewById(R.id.submit_Button);

        initToolbar();
    }

    private void fillStepOne() {
        atticShelveOneLayout.setVisibility(View.VISIBLE);
        atticShelveTwoLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        oneContainerIdEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, oneContainerIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStepTwoLocationLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        atticShelveOneLayout.setVisibility(View.GONE);
        atticShelveTwoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(null);

        twoHeadTextView.setText("确认上架库位");

        twoLocationCodeTextView.setText(curAtticShelve.getLocationCode());
        twoLocationCodeEditText.setText(null);

        twoLocationCodeLayout.setVisibility(View.VISIBLE);
        twoItemNameLayout.setVisibility(View.GONE);
        twoBarcodeLayout.setVisibility(View.GONE);
        twoSkuCodeLayout.setVisibility(View.GONE);
        twoPackNameLayout.setVisibility(View.GONE);
        twoSystemQtyLayout.setVisibility(View.GONE);
        twoInputQtyLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        twoLocationCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, twoLocationCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStepTwoQtyLayout(String locationCode) {
        twoLocationCodeTextView.setText(TextUtils.isEmpty(locationCode) ? curAtticShelve.getLocationCode() : locationCode);

        atticShelveOneLayout.setVisibility(View.GONE);
        atticShelveTwoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        submitButton.setOnClickListener(this);

        twoHeadTextView.setText("确认上架数量");

        twoItemNameTextView.setText(curAtticShelve.getItemName());
        twoBarcodeTextView.setText(curAtticShelve.getBarcode());
        twoSkuCodeTextView.setText(curAtticShelve.getSkuCode());
        twoPackNameTextView.setText(curAtticShelve.getPackName());
        twoAllocQtyTextView.setText(curAtticShelve.getQty());
        twoInputQtyEditView.setHint(curAtticShelve.getQty());
        twoInputQtyEditView.setText(null);

        twoInputQtyEditView.requestFocus();

        twoLocationCodeLayout.setVisibility(View.GONE);
        twoItemNameLayout.setVisibility(View.VISIBLE);
        twoBarcodeLayout.setVisibility(View.VISIBLE);
        twoSkuCodeLayout.setVisibility(View.VISIBLE);
        twoPackNameLayout.setVisibility(View.VISIBLE);
        twoSystemQtyLayout.setVisibility(View.VISIBLE);
        twoInputQtyLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void fillStepThree() {
        fillStepOne();
        curAtticShelve = null;
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
        if (atticShelveOneLayout.getVisibility() == View.VISIBLE) {
            Editable editable = oneContainerIdEditText.getText();
            if (editable != null) {
                String containerId = editable.toString();
                if (!TextUtils.isEmpty(containerId)) {
                    new ScanContainerTask(that, uId, containerId).start();
                }
            }
        } else if (atticShelveTwoLayout.getVisibility() == View.VISIBLE) {
            Editable editable = twoLocationCodeEditText.getText();
            if (editable != null) {
                final String locationCode = editable.toString();
                if (!TextUtils.equals(locationCode, curAtticShelve.getLocationCode())) {
                    DialogTools.showTwoButtonDialog(that, "扫描的库位与目标库位不一致,确认上架", "取消", "确认", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fillStepTwoQtyLayout(locationCode);
                        }
                    }, true);
                } else {
                    fillStepTwoQtyLayout(null);
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
        if (atticShelveTwoLayout.getVisibility() == View.VISIBLE && twoInputQtyLayout.getVisibility() == View.VISIBLE) {
            fillStepTwoLocationLayout();
        } else if (atticShelveTwoLayout.getVisibility() == View.VISIBLE) {
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
            String taskId = curAtticShelve.getTaskId();
            String allocLocationCode = curAtticShelve.getLocationCode();
            String realLocationCode = twoLocationCodeEditText.getText().toString();
            String qty = twoInputQtyEditView.getValue();

            new ScanTargetLocationTask(that, uId, taskId, allocLocationCode, realLocationCode, qty).start();
        }
    }

    private class ScanContainerTask extends HttpAsyncTask<AtticShelve> {

        private String uId;

        private String containerId;

        public ScanContainerTask(Context context, String uId, String containerId) {
            super(context, true, true, false);
            this.uId = uId;
            this.containerId = containerId;
        }

        @Override
        public DataHull<AtticShelve> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(AtticShelve result) {
            curAtticShelve = result;
            fillStepTwoLocationLayout();
        }
    }

    private class ScanTargetLocationTask extends HttpAsyncTask<AtticShelveNext> {

        private String uId;
        private String taskId;
        private String allocLocationCode;
        private String realLocationCode;
        private String qty;

        public ScanTargetLocationTask(Context context, String uId, String taskId, String allocLocationCode, String realLocationCode, String qty) {
            super(context, true, true, false);
            this.uId = uId;
            this.taskId = taskId;
            this.qty = qty;
            this.allocLocationCode = allocLocationCode;
            this.realLocationCode = realLocationCode;
        }

        @Override
        public DataHull<AtticShelveNext> doInBackground() {
            return ScanTargetLocationProvider.request(context, uId, uToken, taskId, allocLocationCode, realLocationCode, qty, serialNumber);
        }

        @Override
        public void onPostExecute(AtticShelveNext result) {
            if (result.isDone()) {
                fillStepThree();
            } else {
                curAtticShelve = result.getAtticShelve();
                fillStepTwoLocationLayout();
            }
        }
    }
}
