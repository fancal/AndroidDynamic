package com.elianshang.wms.app.shelve.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.shelve.R;
import com.elianshang.wms.app.shelve.bean.ResponseState;
import com.elianshang.wms.app.shelve.bean.Shelve;
import com.elianshang.wms.app.shelve.provider.ScanTargetLocationProvider;
import com.xue.http.impl.DataHull;

/**
 * 上架完成页
 */
public class FinishActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken, Shelve shelve) {
        DLIntent intent = new DLIntent(activity.getPackageName(), FinishActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        if (shelve != null) {
            intent.putExtra("shelve", shelve);
        }
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

    /**
     * 上架任务信息
     */
    private Shelve shelve;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    /**
     * 任务TextView
     */
    private TextView taskIdTextView;

    /**
     * 商品名
     */
    private TextView itemNameTextView;

    private TextView barcodeTextView;

    private TextView skuCodeTextView;

    /**
     * 库位TextView
     */
    private TextView locationCodeTextView;

    /**
     * 库位扫描输入框
     */
    private ScanEditText locationCodeEditText;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    private String serialNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelve_activity_finish);

        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        if (readExtras()) {
            findViews();
            fillData();
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
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private boolean readExtras() {
        Intent intent = getIntent();

        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        shelve = (Shelve) intent.getSerializableExtra("shelve");
        return true;
    }

    private void findViews() {
        taskIdTextView = (TextView) findViewById(R.id.taskId_TextView);
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        barcodeTextView = (TextView) findViewById(R.id.barcode_TextView);
        skuCodeTextView = (TextView) findViewById(R.id.skuCode_TextView);
        locationCodeTextView = (TextView) findViewById(R.id.locationCode_TextView);
        locationCodeEditText = (ScanEditText) findViewById(R.id.locationCode_EditText);
        locationCodeEditText.setCode(true);
        scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
        scanEditTextTool.setComplete(this);

        locationCodeEditText.requestFocus();

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

    private void pressBack() {
        DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    private void fillData() {
        if (shelve != null) {
            taskIdTextView.setText(shelve.getTaskId());
            itemNameTextView.setText(shelve.getItemName());
            barcodeTextView.setText(shelve.getBarcode());
            skuCodeTextView.setText(shelve.getSkuCode());
            locationCodeTextView.setText(shelve.getAllocLocationCode());
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onComplete() {
        final String location = locationCodeEditText.getText().toString();
        if (location.equals(shelve.getAllocLocationCode())) {
            new ShelveScanTargetLocationTask(that, shelve.getTaskId(), location).start();
        } else {
            DialogTools.showTwoButtonDialog(that, "扫描库位与目标库位不符,确认上架吗?", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationCodeEditText.setText("");
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ShelveScanTargetLocationTask(FinishActivity.this.that, shelve.getTaskId(), location).start();
                }
            }, false);
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }


    private class ShelveScanTargetLocationTask extends HttpAsyncTask<ResponseState> {

        private String taskId;

        private String locationCode;

        public ShelveScanTargetLocationTask(Context context, String taskId, String locationCode) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ScanTargetLocationProvider.request(context, uId, uToken, taskId, locationCode, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            ToastTool.show(context, "上架完成");
            that.setResult(RESULT_OK);
            finish();
        }
    }

}
