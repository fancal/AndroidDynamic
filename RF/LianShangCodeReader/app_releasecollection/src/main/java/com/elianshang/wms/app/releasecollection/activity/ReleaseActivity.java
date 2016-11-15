package com.elianshang.wms.app.releasecollection.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.releasecollection.R;
import com.elianshang.wms.app.releasecollection.bean.ResponseState;
import com.elianshang.wms.app.releasecollection.provider.ReleaseCollectionRoadProvider;
import com.xue.http.impl.DataHull;

public class ReleaseActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private ScanEditText locationCodeEditText;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.releasecollecttion_activity_main);
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
        if (readExtras()) {
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
        if(scanEditTextTool != null){
            scanEditTextTool.release();
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

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findView() {
        locationCodeEditText = (ScanEditText) findViewById(R.id.locationCode_EditText);
        locationCodeEditText.setCode(true);
        scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
        scanEditTextTool.setComplete(this);

        initToolbar();
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
        final String locationCode = locationCodeEditText.getText().toString();
        DialogTools.showTwoButtonDialog(that, "确认完成一键装车", "取消", "确认", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ReleaseCollectionTask(that, uId, uToken, locationCode).start();
            }
        }, true);
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    /**
     * 释放集货道
     */
    private class ReleaseCollectionTask extends HttpAsyncTask<ResponseState> {

        private String uId;

        private String uToken;

        private String locationCode;

        public ReleaseCollectionTask(Context context, String uId, String uToken, String locationCode) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ReleaseCollectionRoadProvider.request(context, uId, uToken, locationCode, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            finish();
            ToastTool.show(context, "一键装车完成");
        }
    }
}
