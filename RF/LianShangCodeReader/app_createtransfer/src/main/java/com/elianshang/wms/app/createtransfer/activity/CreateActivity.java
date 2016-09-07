package com.elianshang.wms.app.createtransfer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.createtransfer.R;
import com.elianshang.wms.app.createtransfer.bean.LocationView;
import com.elianshang.wms.app.createtransfer.bean.ResponseState;
import com.elianshang.wms.app.createtransfer.provider.CreatePlanProvider;
import com.elianshang.wms.app.createtransfer.provider.ViewLocationProvider;
import com.xue.http.impl.DataHull;

public class CreateActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View createLayout;

    private ScanEditText createLocationIdEditText;

    private View detailLayout;

    private TextView detailLocationCodeTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailSystemQtyTextView;

    private QtyEditText detailInputQtyEditText;

    private Button detailSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private LocationView locationView;

    private String serialNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
        if (readExtras()) {
            findView();
        }

        fillCreate();
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

    @Override
    public void onBackPressed() {
        if (createLayout.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            DialogTools.showTwoButtonDialog(that, "确认放弃创建移库任务", "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        }
    }

    private void findView() {
        createLayout = findViewById(R.id.create_Layout);
        createLocationIdEditText = (ScanEditText) createLayout.findViewById(R.id.locationId_EditText);
        detailLayout = findViewById(R.id.detail_Layout);
        detailLocationCodeTextView = (TextView) detailLayout.findViewById(R.id.locationCode_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailSystemQtyTextView = (TextView) detailLayout.findViewById(R.id.systemQty_TextView);
        detailInputQtyEditText = (QtyEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);

        detailSubmitButton.setOnClickListener(this);

        initToolbar();
    }

    private void fillCreate() {
        createLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        createLocationIdEditText.setText(null);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        scanEditTextTool = new ScanEditTextTool(that, createLocationIdEditText);
        scanEditTextTool.setComplete(this);

    }

    private void fillDetail() {
        if (locationView == null) {
            return;
        }
        createLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailLocationCodeTextView.setText(locationView.getLocationCode());
        detailItemNameTextView.setText(locationView.getItemName());
        detailPackNameTextView.setText(locationView.getPackName());
        detailSystemQtyTextView.setText(locationView.getUomQty());
        detailInputQtyEditText.setText(null);
        detailInputQtyEditText.setHint(locationView.getUomQty());
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onClick(View v) {
        if (v == detailSubmitButton) {
            String locationId = locationView.getLocationId();
            String qty = detailInputQtyEditText.getValue();

            new CreatePlanTask(that, uId, uToken, locationId, qty).start();
        }
    }

    @Override
    public void onComplete() {
        if (createLayout.getVisibility() == View.VISIBLE) {
            String locationId = createLocationIdEditText.getText().toString();
            new ViewLocationTask(that, uId, uToken, locationId).start();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    /**
     * 查询库位信息
     */
    private class ViewLocationTask extends HttpAsyncTask<LocationView> {

        private String uId;

        private String uToken;

        private String locationId;

        public ViewLocationTask(Context context, String uId, String uToken, String locationId) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationId = locationId;
        }

        @Override
        public DataHull<LocationView> doInBackground() {
            return ViewLocationProvider.request(context, uId, uToken, locationId);
        }

        @Override
        public void onPostExecute(LocationView result) {
            locationView = result;
            fillDetail();
        }
    }

    /**
     * 创建移库任务
     */
    private class CreatePlanTask extends HttpAsyncTask<ResponseState> {

        private String uId;

        private String uToken;

        private String locationId;

        private String qty;

        public CreatePlanTask(Context context, String uId, String uToken, String locationId, String qty) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationId = locationId;
            this.qty = qty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return CreatePlanProvider.request(context, uId, uToken, locationId, qty, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            finish();
            ToastTool.show(context, "创建移库任务完成");
        }
    }
}
