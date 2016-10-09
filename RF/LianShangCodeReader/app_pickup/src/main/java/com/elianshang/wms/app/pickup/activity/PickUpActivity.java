package com.elianshang.wms.app.pickup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.pickup.R;
import com.elianshang.wms.app.pickup.bean.PickUpView;
import com.elianshang.wms.app.pickup.bean.ResponseState;
import com.elianshang.wms.app.pickup.provider.PickUpDoProvider;
import com.elianshang.wms.app.pickup.provider.PickUpViewProvider;
import com.xue.http.impl.DataHull;

public class PickUpActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    private Toolbar mToolbar;

    private View scanLayout;

    private ScanEditText scanContainerIdEditText;

    private View viewLayout;

    private TextView viewContainerIdTextView;

    private TextView viewLocationCodeTextView;

    private TextView viewStatusTextView;

    private Button viewSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private String uId;

    private String uToken;

    private PickUpView pickUpView;

    private String serialNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pickup);

        if (readExtra()) {
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
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findView() {
        initToolbar();

        scanLayout = findViewById(R.id.scan_Layout);
        scanContainerIdEditText = (ScanEditText) scanLayout.findViewById(R.id.containerId_EditText);

        viewLayout = findViewById(R.id.view_Layout);
        viewContainerIdTextView = (TextView) viewLayout.findViewById(R.id.containerId_TextView);
        viewLocationCodeTextView = (TextView) viewLayout.findViewById(R.id.locationCode_TextView);
        viewStatusTextView = (TextView) viewLayout.findViewById(R.id.status_TextView);
        viewSubmitButton = (Button) viewLayout.findViewById(R.id.submit_Button);

        viewSubmitButton.setOnClickListener(this);
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

    private void fillScan() {
        scanLayout.setVisibility(View.VISIBLE);
        viewLayout.setVisibility(View.GONE);

        scanContainerIdEditText.setText(null);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanContainerIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillView(PickUpView pickUpView) {
        this.pickUpView = pickUpView;
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        viewLayout.setVisibility(View.VISIBLE);

        if (pickUpView != null) {
            viewContainerIdTextView.setText(pickUpView.getContainerId());
            viewLocationCodeTextView.setText(pickUpView.getLocationCode());

            if ("2".equals(pickUpView.getStatus())) {
                viewStatusTextView.setText("已集货");
                viewSubmitButton.setText("知道了");
            } else {
                viewStatusTextView.setText("未集货");
                viewSubmitButton.setText("集货");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == viewSubmitButton) {
            if ("2".equals(pickUpView.getStatus())) {
                fillScan();
            } else {
                new RequestPickUpDoTask(that, pickUpView.getContainerId()).start();
            }
        }
    }

    @Override
    public void onComplete() {
        String containerId = scanContainerIdEditText.getText().toString();
        if (!TextUtils.isEmpty(containerId)) {
            new RequestPickUpViewTask(that, containerId).start();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        }
    }

    private class RequestPickUpViewTask extends HttpAsyncTask<PickUpView> {

        private String containerId;

        public RequestPickUpViewTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<PickUpView> doInBackground() {
            return PickUpViewProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(PickUpView result) {
            fillView(result);
        }
    }

    private class RequestPickUpDoTask extends HttpAsyncTask<ResponseState> {

        private String containerId;

        public RequestPickUpDoTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return PickUpDoProvider.request(context, uId, uToken, containerId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillScan();
        }
    }
}
