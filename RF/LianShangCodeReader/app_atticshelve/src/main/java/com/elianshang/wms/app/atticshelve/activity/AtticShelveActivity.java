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
import com.elianshang.wms.app.atticshelve.R;
import com.elianshang.wms.app.atticshelve.bean.AtticShelve;
import com.elianshang.wms.app.atticshelve.bean.AtticShelveNext;
import com.elianshang.wms.app.atticshelve.provider.ScanContainerProvider;
import com.elianshang.wms.app.atticshelve.provider.ScanTargetLocationProvider;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.xue.http.impl.DataHull;

public class AtticShelveActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private View atticShelveOneLayout;

    private ScanEditText oneContainerIdEditText;

    private View atticShelveTwoLayout;

    private TextView twoHeadTextView;

    private View twoLocationIdLayout;

    private TextView twoLocationCodeTextView;

    private ScanEditText twoLocationIdEditText;

    private View twoPackNameLayout;

    private TextView twoPackNameTextView;

    private View twoSystemQtyLayout;

    private TextView twoAllocQtyTextView;

    private View twoInputQtyLayout;

    private QtyEditText twoInputQtyEditView;

    private View atticShelveThreeLayout;

    private Button submitButton;

    private ScanEditTextTool scanEditTextTool;

    private AtticShelve curAtticShelve;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atticshelve);
        findViews();

        readExtra();

        fillStepOne();
    }

    private void readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "243202523137671";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
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

    private void findViews() {

        atticShelveOneLayout = findViewById(R.id.atticshelve_one);
        oneContainerIdEditText = (ScanEditText) atticShelveOneLayout.findViewById(R.id.containerId_EditText);

        atticShelveTwoLayout = findViewById(R.id.atticshelve_two);
        twoHeadTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.head_TextView);
        twoLocationIdLayout = atticShelveTwoLayout.findViewById(R.id.locationId_Layout);
        twoLocationCodeTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.locationCode_TextView);
        twoLocationIdEditText = (ScanEditText) atticShelveTwoLayout.findViewById(R.id.locationId_EditText);
        twoPackNameLayout = atticShelveTwoLayout.findViewById(R.id.packName_Layout);
        twoPackNameTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.packName_TextView);
        twoSystemQtyLayout = atticShelveTwoLayout.findViewById(R.id.systemQty_Layout);
        twoAllocQtyTextView = (TextView) atticShelveTwoLayout.findViewById(R.id.allocQty_TextView);
        twoInputQtyLayout = atticShelveTwoLayout.findViewById(R.id.inputQty_Layout);
        twoInputQtyEditView = (QtyEditText) atticShelveTwoLayout.findViewById(R.id.inputQty_EditView);

        atticShelveThreeLayout = findViewById(R.id.atticshelve_three);

        submitButton = (Button) findViewById(R.id.submit_Button);

        initToolbar();
    }

    private void fillStepOne() {
        atticShelveOneLayout.setVisibility(View.VISIBLE);
        atticShelveTwoLayout.setVisibility(View.GONE);
        atticShelveThreeLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        scanEditTextTool = new ScanEditTextTool(that, oneContainerIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStepTwoLocationLayout() {
        atticShelveOneLayout.setVisibility(View.GONE);
        atticShelveTwoLayout.setVisibility(View.VISIBLE);
        atticShelveThreeLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(null);

        twoHeadTextView.setText("确认上架库位");

        twoLocationCodeTextView.setText(curAtticShelve.getLocationCode());
        twoLocationIdEditText.setText(null);

        twoLocationIdLayout.setVisibility(View.VISIBLE);
        twoPackNameLayout.setVisibility(View.GONE);
        twoSystemQtyLayout.setVisibility(View.GONE);
        twoInputQtyLayout.setVisibility(View.GONE);

        scanEditTextTool = new ScanEditTextTool(that, twoLocationIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStepTwoQtyLayout() {
        atticShelveOneLayout.setVisibility(View.GONE);
        atticShelveTwoLayout.setVisibility(View.VISIBLE);
        atticShelveThreeLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);

        submitButton.setOnClickListener(this);

        twoHeadTextView.setText("确认上架数量");

        twoPackNameTextView.setText(curAtticShelve.getPackName());
        twoAllocQtyTextView.setText(curAtticShelve.getQty());
        twoInputQtyEditView.setHint(curAtticShelve.getQty());
        twoInputQtyEditView.setText(null);

        twoLocationIdLayout.setVisibility(View.GONE);
        twoPackNameLayout.setVisibility(View.VISIBLE);
        twoSystemQtyLayout.setVisibility(View.VISIBLE);
        twoInputQtyLayout.setVisibility(View.VISIBLE);

        scanEditTextTool.setComplete(null);
    }

    private void fillStepThree() {
        atticShelveOneLayout.setVisibility(View.GONE);
        atticShelveTwoLayout.setVisibility(View.GONE);
        atticShelveThreeLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
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
            Editable editable = twoLocationIdEditText.getText();
            if (editable != null) {
                final String locationId = editable.toString();
                if (!TextUtils.isEmpty(locationId)) {
                    DialogTools.showTwoButtonDialog(that, "扫描的库位与目标库位不一致,确认上架", "取消", "确认", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fillStepTwoQtyLayout();
                        }
                    }, true);
                } else {
                    fillStepTwoQtyLayout();
                }
            }
        }
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            String taskId = curAtticShelve.getTaskId();
            String allocLocationId = curAtticShelve.getLocationId();
            String realLocationId = twoLocationIdEditText.getText().toString();
            String qty = twoInputQtyEditView.getValue();

            new ScanTargetLocationTask(that, uId, taskId, allocLocationId, realLocationId, qty).start();
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
            return ScanContainerProvider.request(uId, containerId);
        }

        @Override
        public void onPostExecute(int updateId, AtticShelve result) {
            curAtticShelve = result;
            fillStepTwoLocationLayout();
        }
    }

    private class ScanTargetLocationTask extends HttpAsyncTask<AtticShelveNext> {

        private String uId;
        private String taskId;
        private String allocLocationId;
        private String realLocationId;
        private String qty;

        public ScanTargetLocationTask(Context context, String uId, String taskId, String allocLocationId, String realLocationId, String qty) {
            super(context, true, true, false);
            this.uId = uId;
            this.taskId = taskId;
            this.qty = qty;
            this.allocLocationId = allocLocationId;
            this.realLocationId = realLocationId;
        }

        @Override
        public DataHull<AtticShelveNext> doInBackground() {
            return ScanTargetLocationProvider.request(uId, taskId, allocLocationId, realLocationId, qty);
        }

        @Override
        public void onPostExecute(int updateId, AtticShelveNext result) {
            if (result.isDone()) {
                fillStepThree();
            } else {
                curAtticShelve = result.getAtticShelve();
                fillStepTwoLocationLayout();
            }
        }
    }
}
