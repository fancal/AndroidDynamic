package com.elianshang.wms.app.releasecollection.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.releasecollection.R;
import com.elianshang.wms.app.releasecollection.bean.CollectionRoadDetail;
import com.elianshang.wms.app.releasecollection.bean.ResponseState;
import com.elianshang.wms.app.releasecollection.provider.CollectionRoadDetailProvider;
import com.elianshang.wms.app.releasecollection.provider.ReleaseCollectionRoadProvider;
import com.xue.http.impl.DataHull;

public class ReleaseActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View scanLayout;

    private View detailLayout;

    private ScanEditText scanLocationCodeEditText;

    private TextView detailCollectionRoadTextView;

    private TextView detailTuIdTextView;

    private TextView detailDriverNameTextView;

    private TextView detailCarNumberTextView;

    private TextView detailStoreNumTextView;

    private TextView detailPackCountTextView;

    private TextView detailTurnoverBoxCountTextView;

    private Button detailSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;

    private CollectionRoadDetail detail;

    private boolean isItemClick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.releasecollecttion_activity_main);
        if (readExtras()) {
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

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        //FIXME
//        uId = "97895189586439";
//        uToken = "25061134202027";
//        ScanManager.init(that);

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
        scanLayout = findViewById(R.id.scan_Layout);
        scanLocationCodeEditText = (ScanEditText) scanLayout.findViewById(R.id.locationCode_EditText);
        scanLocationCodeEditText.setCode(true);

        detailLayout = findViewById(R.id.detail_Layout);
        detailCollectionRoadTextView = (TextView) detailLayout.findViewById(R.id.collectionRoad_TextView);
        detailTuIdTextView = (TextView) detailLayout.findViewById(R.id.tuId_TextView);
        detailDriverNameTextView = (TextView) detailLayout.findViewById(R.id.driverName_TextView);
        detailCarNumberTextView = (TextView) detailLayout.findViewById(R.id.carNumber_TextView);
        detailStoreNumTextView = (TextView) detailLayout.findViewById(R.id.storeNum_TextView);
        detailPackCountTextView = (TextView) detailLayout.findViewById(R.id.packCount_TextView);
        detailTurnoverBoxCountTextView = (TextView) detailLayout.findViewById(R.id.turnoverBoxCount_TextView);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);

        detailSubmitButton.setOnClickListener(this);
        initToolbar();
    }

    private void fillScan() {
        scanLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);
        scanLocationCodeEditText.setText("");
        detail = null;

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanLocationCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillDetail() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);


        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        if (detail == null) {
            return;
        }

        detailCollectionRoadTextView.setText("集货道：" + scanLocationCodeEditText.getText().toString());
        detailTuIdTextView.setText("路线编码：" + detail.getTransPlan());
        detailDriverNameTextView.setText("司机：" + detail.getDriverName());
        detailCarNumberTextView.setText("车牌号：" + detail.getCarNumber());
        detailStoreNumTextView.setText("门店数：" + detail.getCustomerCount());
        detailPackCountTextView.setText("箱数：" + detail.getPackCount());
        detailTurnoverBoxCountTextView.setText("周转箱数：" + detail.getTurnoverBoxNum());
    }

    @Override
    public void onBackPressed() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            finish();
        } else if (detailLayout.getVisibility() == View.VISIBLE) {
            fillScan();
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
        final String locationCode = scanLocationCodeEditText.getText().toString();
        new CollectionDetailTask(that, uId, uToken, locationCode).start();
    }

    @Override
    public void onError(ContentEditText editText) {

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

        if (v == detailSubmitButton) {
            final String locationCode = scanLocationCodeEditText.getText().toString();
            new ReleaseCollectionTask(that, uId, uToken, locationCode).start();
        }
    }

    /**
     * 释放集货道
     */
    private class CollectionDetailTask extends HttpAsyncTask<CollectionRoadDetail> {

        private String uId;

        private String uToken;

        private String locationCode;

        public CollectionDetailTask(Context context, String uId, String uToken, String locationCode) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<CollectionRoadDetail> doInBackground() {
            return CollectionRoadDetailProvider.request(context, uId, uToken, locationCode);
        }

        @Override
        public void onPostExecute(CollectionRoadDetail result) {
            detail = result;
            fillDetail();
        }
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
            ToastTool.show(context, "装车成功");
        }
    }
}