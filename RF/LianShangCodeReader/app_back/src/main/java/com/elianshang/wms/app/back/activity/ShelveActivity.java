package com.elianshang.wms.app.back.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

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
import com.elianshang.wms.app.back.provider.ShelveConfirmProvider;
import com.elianshang.wms.app.back.provider.ShelveTransferOutProvider;
import com.xue.http.impl.DataHull;


/**
 * 退货入库
 */
public class ShelveActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ShelveActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }


    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View scanLayout;

    private ScanEditText scanSoOtherIdEditText;

    private View confirmLayout;

    private ScanEditText confirmLocationEditText;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelve);
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

        confirmLayout = findViewById(R.id.confirm_Layout);
        confirmLocationEditText = (ScanEditText) confirmLayout.findViewById(R.id.locationCode_EditText);

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
        confirmLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanSoOtherIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillConfirmLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, confirmLocationEditText);
        scanEditTextTool.setComplete(this);

    }

    @Override
    public void onBackPressed() {
        if (confirmLayout.getVisibility() == View.VISIBLE) {
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
                new ScanLocationTask(that, editable.toString()).start();
            }
        } else if (confirmLayout.getVisibility() == View.VISIBLE) {
            Editable editable = confirmLocationEditText.getText();
            if (editable != null) {
                new ConfirmTask(that, editable.toString(), serialNumber).start();
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

    private class ScanLocationTask extends HttpAsyncTask<ResponseState> {

        String locationCode;

        public ScanLocationTask(Context context, String locationCode) {
            super(context, true, true);
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ShelveTransferOutProvider.request(context, uId, uToken, locationCode);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillConfirmLayout();
        }
    }

    private class ConfirmTask extends HttpAsyncTask<ResponseState> {

        String locationCode;

        String serialNumber;

        public ConfirmTask(Context context, String locationCode, String serialNumber) {
            super(context, true, true, false);
            this.locationCode = locationCode;
            this.serialNumber = serialNumber;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ShelveConfirmProvider.request(context, uId, uToken, locationCode, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            ToastTool.show(context, "退货上架成功!");
            finish();
        }
    }
}
