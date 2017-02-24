package com.elianshang.wms.app.capacity.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.capacity.R;
import com.elianshang.wms.app.capacity.bean.PartBean;
import com.elianshang.wms.app.capacity.bean.ResponseState;
import com.elianshang.wms.app.capacity.provider.PartProvider;
import com.elianshang.wms.app.capacity.provider.ViewProvider;
import com.xue.http.impl.DataHull;

/**
 * 盘点页面
 */
public class CapacityPartActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), CapacityPartActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private ScanEditText locationCodeEditText;

    private TextView statusTextView;

    private TextView infoTextView;

    /**
     * 提交
     */
    private Button submitButton;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;

    private boolean isItemClick = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capacity_part_activity_main);

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
    }

    private void findView() {
        locationCodeEditText = (ScanEditText) findViewById(R.id.locationCode_EditText);
        locationCodeEditText.setCode(true);
        statusTextView = (TextView) findViewById(R.id.status_TextView);
        infoTextView = (TextView) findViewById(R.id.info_TextView);
        submitButton = (Button) findViewById(R.id.submit_Button);

        submitButton.setOnClickListener(this);
        submitButton.setVisibility(View.GONE);

        scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
        scanEditTextTool.setComplete(this);

        initToolbar();
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

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillPratBean(PartBean partBean) {
        if (partBean.canSplit()) {
            statusTextView.setVisibility(View.VISIBLE);
            infoTextView.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);

            statusTextView.setText("库位可拆分");
            infoTextView.setText("关联库位：\n");
            for (String locationCode : partBean.getCodes()) {
                infoTextView.append("\t" + locationCode + "\n");
            }

            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
        } else {
            statusTextView.setText("库位不可拆分（" + partBean.getMsg() + "）");

            statusTextView.setVisibility(View.VISIBLE);
            infoTextView.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        }
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

        if (submitButton == v) {
            String locationCode = locationCodeEditText.getText().toString();
            new PartTask(that, locationCode).start();
        }
    }

    @Override
    public void onComplete() {
        scanEditTextTool.release();
        scanEditTextTool = null;

        String locationCode = locationCodeEditText.getText().toString();
        new ViewTask(that, locationCode).start();
    }

    @Override
    public void onError(ContentEditText editText) {

    }


    /**
     * 查询
     */
    private class ViewTask extends HttpAsyncTask<PartBean> {

        private String locationCode;

        private ViewTask(Context context, String locationCode) {
            super(context, true, true);
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<PartBean> doInBackground() {
            return ViewProvider.request(context, uId, uToken, locationCode);
        }

        @Override
        public void onPostExecute(PartBean result) {
            fillPratBean(result);
            scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
            scanEditTextTool.setComplete(CapacityPartActivity.this);
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
            scanEditTextTool.setComplete(CapacityPartActivity.this);
        }

        @Override
        public void netNull() {
            super.netNull();
            scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
            scanEditTextTool.setComplete(CapacityPartActivity.this);
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
            scanEditTextTool.setComplete(CapacityPartActivity.this);
        }
    }

    /**
     * 拆分
     */
    private class PartTask extends HttpAsyncTask<ResponseState> {

        private String locationCode;

        private PartTask(Context context, String locationCode) {
            super(context, true, true);
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return PartProvider.request(context, uId, uToken, locationCode, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            finish();
        }
    }
}