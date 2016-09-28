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
import com.elianshang.wms.app.sow.provider.ScanContainerProvider;
import com.elianshang.wms.app.sow.provider.ScanTargetContainerProvider;
import com.xue.http.impl.DataHull;

public class SowActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Sow sow) {
        DLIntent intent = new DLIntent(activity.getPackageName(), SowActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (sow != null) {
            intent.putExtra("sow", sow);
        }
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private View sowOneLayout;

    private ScanEditText oneContainerIdEditText;

    private View sowTwoLayout;

    private TextView twoHeadTextView;

    private View twoContainerIdLayout;

    private TextView twoContainerIdTextView;

    private ScanEditText twoContainerIdEditText;

    private View twoItemNameLayout;

    private TextView twoItemNameTextView;

    private View twoPackNameLayout;

    private TextView twoPackNameTextView;

    private View twoSystemQtyLayout;

    private TextView twoAllocQtyTextView;

    private View twoInputQtyLayout;

    private QtyEditText twoInputQtyEditView;

    private Button submitButton;

    private ScanEditTextTool scanEditTextTool;

    private Sow curSow;

    private String serialNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sow);
        findViews();

        if (readExtra()) {
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        curSow = (Sow) intent.getSerializableExtra("sow");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        if (curSow != null) {
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
        sowOneLayout = findViewById(R.id.sow_one);
        oneContainerIdEditText = (ScanEditText) sowOneLayout.findViewById(R.id.containerId_EditText);

        sowTwoLayout = findViewById(R.id.sow_two);
        twoHeadTextView = (TextView) sowTwoLayout.findViewById(R.id.head_TextView);
        twoContainerIdLayout = sowTwoLayout.findViewById(R.id.containerId_Layout);
        twoContainerIdTextView = (TextView) sowTwoLayout.findViewById(R.id.containerId_TextView);
        twoContainerIdEditText = (ScanEditText) sowTwoLayout.findViewById(R.id.containerId_EditText);
        twoContainerIdEditText.setCode(true);
        twoItemNameLayout = sowTwoLayout.findViewById(R.id.itemName_Layout);
        twoItemNameTextView = (TextView) sowTwoLayout.findViewById(R.id.itemName_TextView);
        twoPackNameLayout = sowTwoLayout.findViewById(R.id.packName_Layout);
        twoPackNameTextView = (TextView) sowTwoLayout.findViewById(R.id.packName_TextView);
        twoSystemQtyLayout = sowTwoLayout.findViewById(R.id.systemQty_Layout);
        twoAllocQtyTextView = (TextView) sowTwoLayout.findViewById(R.id.allocQty_TextView);
        twoInputQtyLayout = sowTwoLayout.findViewById(R.id.inputQty_Layout);
        twoInputQtyEditView = (QtyEditText) sowTwoLayout.findViewById(R.id.inputQty_EditView);

        submitButton = (Button) findViewById(R.id.submit_Button);

        initToolbar();
    }

    private void fillStepOne() {
        sowOneLayout.setVisibility(View.VISIBLE);
        sowTwoLayout.setVisibility(View.GONE);
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

        sowOneLayout.setVisibility(View.GONE);
        sowTwoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(null);

        twoHeadTextView.setText("确认播种托盘码");

        twoContainerIdTextView.setText(curSow.getContainerId());
        twoContainerIdEditText.setText(null);

        twoContainerIdLayout.setVisibility(View.VISIBLE);
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
        sowOneLayout.setVisibility(View.GONE);
        sowTwoLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        submitButton.setOnClickListener(this);

        twoHeadTextView.setText("确认播种数量");

        twoItemNameTextView.setText(curSow.getItemName());
        twoPackNameTextView.setText(curSow.getPackName());
        twoAllocQtyTextView.setText(curSow.getQty());
        twoInputQtyEditView.setHint(curSow.getQty());
        twoInputQtyEditView.setText(null);

        twoInputQtyEditView.requestFocus();

        twoContainerIdLayout.setVisibility(View.GONE);
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
        if (sowOneLayout.getVisibility() == View.VISIBLE) {
            Editable editable = oneContainerIdEditText.getText();
            if (editable != null) {
                String containerId = editable.toString();
                if (!TextUtils.isEmpty(containerId)) {
                    new ScanContainerTask(that, uId, containerId).start();
                }
            }
        } else if (sowTwoLayout.getVisibility() == View.VISIBLE) {
            Editable editable = twoContainerIdEditText.getText();
            if (editable != null) {
                final String containerId = editable.toString();
                if (!TextUtils.equals(containerId, curSow.getContainerId())) {
                    DialogTools.showTwoButtonDialog(that, "扫描的托盘码与目标托盘码不一致,确认播种", "取消", "确认", null, new DialogInterface.OnClickListener() {
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
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onBackPressed() {
        if (sowTwoLayout.getVisibility() == View.VISIBLE) {
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
            String allocContainerId = curSow.getContainerId();
            String realContainerId = twoContainerIdEditText.getText().toString();
            String qty = twoInputQtyEditView.getValue();

            new ScanTargetLocationTask(that, uId, taskId, allocContainerId, realContainerId, qty).start();
        }
    }

    private class ScanContainerTask extends HttpAsyncTask<Sow> {

        private String uId;

        private String containerId;

        public ScanContainerTask(Context context, String uId, String containerId) {
            super(context, true, true, false);
            this.uId = uId;
            this.containerId = containerId;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return ScanContainerProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            fillStepTwoLocationLayout();
        }
    }

    private class ScanTargetLocationTask extends HttpAsyncTask<SowNext> {

        private String uId;
        private String taskId;
        private String allocContainerId;
        private String realContainerId;
        private String qty;

        public ScanTargetLocationTask(Context context, String uId, String taskId, String allocContainerId, String realContainerId, String qty) {
            super(context, true, true, false);
            this.uId = uId;
            this.taskId = taskId;
            this.qty = qty;
            this.allocContainerId = allocContainerId;
            this.realContainerId = realContainerId;
        }

        @Override
        public DataHull<SowNext> doInBackground() {
            return ScanTargetContainerProvider.request(context, uId, uToken, taskId, allocContainerId, realContainerId, qty, serialNumber);
        }

        @Override
        public void onPostExecute(SowNext result) {
            if (result.isDone()) {
                fillStepThree();
            } else {
                curSow = result.getSow();
                fillStepTwoLocationLayout();
            }
        }
    }
}
