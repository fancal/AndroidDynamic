package com.elianshang.wms.app.setofgoods.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.setofgoods.R;
import com.elianshang.wms.app.setofgoods.bean.SetOfGoodsView;
import com.elianshang.wms.app.setofgoods.bean.ResponseState;
import com.elianshang.wms.app.setofgoods.provider.SetOfGoodsDoProvider;
import com.elianshang.wms.app.setofgoods.provider.SetOfGoodsViewProvider;
import com.xue.http.impl.DataHull;

public class SetOfGoodsActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    private Toolbar mToolbar;

    private View scanLayout;

    private ScanEditText scanContainerIdEditText;

    private View viewLayout;

    private TextView viewStoreNameTextView;

    private TextView viewStoreNoTextView;

    private TextView viewContainerIdTextView;

    private TextView viewLocationCodeTextView;

    private TextView viewStatusTextView;

    private Button viewSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private String uId;

    private String uToken;

    private SetOfGoodsView setOfGoodsView;

    private String serialNumber;

    private boolean isItemClick = false ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setofgoods_activity_main);

        if (readExtra()) {
            findView();
            fillScan();
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
        viewStoreNameTextView = (TextView) viewLayout.findViewById(R.id.storeName_TextView);
        viewStoreNoTextView = (TextView) viewLayout.findViewById(R.id.storeNo_TextView);
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

    private void fillView(SetOfGoodsView setOfGoodsView) {
        this.setOfGoodsView = setOfGoodsView;
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        viewLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        if (setOfGoodsView != null) {
            viewStoreNameTextView.setText(setOfGoodsView.getCustomerName());
            viewStoreNoTextView.setText(setOfGoodsView.getCustomerCode());
            viewContainerIdTextView.setText(setOfGoodsView.getContainerId());
            viewLocationCodeTextView.setText(setOfGoodsView.getLocationCode());

            if ("2".equals(setOfGoodsView.getStatus())) {
                viewStatusTextView.setText("已集货");
                viewSubmitButton.setText("知道了");
            } else {
                viewStatusTextView.setText("未集货");
                viewSubmitButton.setText("集货");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (viewLayout.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(that, "是否退出集货", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
            return;
        }

        finish();
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

        if (v == viewSubmitButton) {
            if ("2".equals(setOfGoodsView.getStatus())) {
                fillScan();
            } else {
                new RequestPickUpDoTask(that, setOfGoodsView.getContainerId()).start();
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

    private class RequestPickUpViewTask extends HttpAsyncTask<SetOfGoodsView> {

        private String containerId;

        public RequestPickUpViewTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<SetOfGoodsView> doInBackground() {
            return SetOfGoodsViewProvider.request(context, uId, uToken, containerId);
        }

        @Override
        public void onPostExecute(SetOfGoodsView result) {
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
            return SetOfGoodsDoProvider.request(context, uId, uToken, containerId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillScan();
        }
    }
}
