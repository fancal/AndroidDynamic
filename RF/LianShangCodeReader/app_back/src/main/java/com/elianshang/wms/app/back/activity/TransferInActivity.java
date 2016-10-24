package com.elianshang.wms.app.back.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.bean.ResponseState;
import com.elianshang.wms.app.back.bean.SupplierInfo;
import com.elianshang.wms.app.back.provider.TransferInConfirmProvider;
import com.elianshang.wms.app.back.provider.TransferInScanProvider;
import com.xue.http.impl.DataHull;


/**
 * 退货入库
 */
public class TransferInActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TransferInActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View scanLayout;

    private ScanEditText scanSoOtherIdEditText;

    private View startLayout;

    private TextView startSupplierNameTextView;

    private TextView startLocationCodeTextView;

    private ScanEditText startLocationConfirmEditText;

    private SupplierInfo supplierInfo;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferin);
        if (readExtras()) {
            findView();
            fillScanLayout();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findView() {
        initToolbar();


        scanLayout = findViewById(R.id.scan_Layout);
        scanSoOtherIdEditText = (ScanEditText) scanLayout.findViewById(R.id.unknownCode_EditText);

        startLayout = findViewById(R.id.start_Layout);
        startSupplierNameTextView = (TextView) startLayout.findViewById(R.id.supplierName_TextView);
        startLocationCodeTextView = (TextView) startLayout.findViewById(R.id.location_id);
        startLocationConfirmEditText = (ScanEditText) startLayout.findViewById(R.id.confirm_location_id);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fillScanLayout() {
        scanLayout.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanSoOtherIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStartLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, startLocationConfirmEditText);
        scanEditTextTool.setComplete(this);

        startSupplierNameTextView.setText(supplierInfo.getSupplierName());
        startLocationCodeTextView.setText(supplierInfo.getLocationCode());
    }

    private void fillConfirm() {
        scanSoOtherIdEditText.setText(null);
        startLocationConfirmEditText.setText(null);
        fillScanLayout();
    }

    @Override
    public void onBackPressed() {
        if (startLayout.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将重新开始", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        } else {
            super.onBackPressed();
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


    @Override
    public void onComplete() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            Editable editable = scanSoOtherIdEditText.getText();
            if (editable != null) {
                new ScanTask(that, editable.toString()).start();
            }
        } else if (startLayout.getVisibility() == View.VISIBLE) {
            Editable editable = startLocationConfirmEditText.getText();
            if (editable != null) {
                if (TextUtils.equals(startLocationCodeTextView.getText(), editable.toString())) {
                    new ConfirmTask(that, editable.toString(), supplierInfo.getTaskId(), serialNumber).start();
                } else {
                    ToastTool.show(that, "库位码不一致,请重新扫描");
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

    private class ScanTask extends HttpAsyncTask<SupplierInfo> {

        String soOtherId;

        public ScanTask(Context context, String soOtherId) {
            super(context, true, true);
            this.soOtherId = soOtherId;
        }

        @Override
        public DataHull<SupplierInfo> doInBackground() {
            return TransferInScanProvider.request(context, uId, uToken, soOtherId);
        }

        @Override
        public void onPostExecute(SupplierInfo result) {
            supplierInfo = result;
            fillStartLayout();
        }
    }

    private class ConfirmTask extends HttpAsyncTask<ResponseState> {

        String locationCode;

        String taskId;

        String serialNumber;

        public ConfirmTask(Context context, String locationCode, String taskId, String serialNumber) {
            super(context, true, true);
            this.locationCode = locationCode;
            this.taskId = taskId;
            this.serialNumber = serialNumber;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return TransferInConfirmProvider.request(context, uId, uToken, locationCode, taskId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillConfirm();
        }
    }

}
