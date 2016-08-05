package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 补货
 */
public class ProcurementActivity extends BaseActivity implements ScanEditTextTool.OnSetComplete, ScanManager.OnBarCodeListener {

    public static void launch(Context context, TaskTransferDetail transferDetail, boolean isTransferFrom) {
        Intent intent = new Intent(context, ProcurementActivity.class);
        intent.putExtra("transferDetail", transferDetail);
        intent.putExtra("isTransferFrom", isTransferFrom);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private TextView mTaskView;
    private TextView mProductNameView;
    private TextView mProductPackNameView;
    private TextView mProductQtyView;
    private EditText mProductQtyRealView;
    private View mProductQtyRealContainerView;
    private TextView mLocationIdView;
    private ScanEditText mLocationIdConfirmView;
    private TextView mTransferTypeNameView;
    private ScanEditTextTool scanEditTextTool;
    private Button button;
    private View mProductView;
    private View mLocationView;
    private TextView mProductLocationView;

    private TaskTransferDetail transferDetail;
    /**
     * from 转出
     */
    private boolean isTransferFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_location);
        readExtras();
        findViews();
        fill();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }

    @Override
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(this, "任务未完成，确认退出？", "取消", "确认", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    private void readExtras() {
        isTransferFrom = getIntent().getBooleanExtra("isTransferFrom", false);
        transferDetail = (TaskTransferDetail) getIntent().getSerializableExtra("transferDetail");
    }

    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTaskView = (TextView) findViewById(R.id.task_id);
        mProductNameView = (TextView) findViewById(R.id.product_name);
        mProductPackNameView = (TextView) findViewById(R.id.product_pack_name);
        mProductQtyView = (TextView) findViewById(R.id.product_qty);
        mProductQtyRealView = (EditText) findViewById(R.id.product_qty_real);
        mProductQtyRealContainerView = findViewById(R.id.product_qty_real_container);
        mLocationIdView = (TextView) findViewById(R.id.location_id);
        mLocationIdConfirmView = (ScanEditText) findViewById(R.id.confirm_location_id);
        button = (Button) findViewById(R.id.button);
        mTransferTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mProductView = findViewById(R.id.product);
        mLocationView = findViewById(R.id.location);
        mProductLocationView = (TextView) findViewById(R.id.product_locationId);

        mTransferTypeNameView.setText(isTransferFrom ? "转出" : "转入");
        scanEditTextTool = new ScanEditTextTool(this, mLocationIdConfirmView);
        scanEditTextTool.setComplete(this);

        button.setEnabled(false);
        button.setClickable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = isTransferFrom ? mProductQtyRealView.getText().toString() : transferDetail.getUomQty();
                new RequestTransferTask(ProcurementActivity.this, transferDetail.getTaskId(), isTransferFrom ? transferDetail.getFromLocationId() : transferDetail.getToLocationId(), qty, transferDetail.getProductPackName(), isTransferFrom).start();

            }
        });
        mProductQtyRealView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean check = !TextUtils.isEmpty(mProductQtyRealView.getText().toString());
                button.setEnabled(check);
                button.setClickable(check);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void fill() {
        mTaskView.setText("任务ID：" + transferDetail.getTaskId());
        mProductNameView.setText("商品名称：" + transferDetail.getProductName());
        mProductPackNameView.setText("包装单位：" + transferDetail.getProductPackName());
        mProductQtyView.setText((isTransferFrom ? "商品数量：" : "实际数量：") + transferDetail.getUomQty());
        mLocationIdView.setText(isTransferFrom ? transferDetail.getFromLocationName() : transferDetail.getToLocationName());
        mProductQtyRealContainerView.setVisibility(isTransferFrom ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSetComplete() {
        String scanLocationId = mLocationIdConfirmView.getText().toString();
        scanLocationId = isTransferFrom ? "14" : "18";
        boolean check = TextUtils.equals(isTransferFrom ? transferDetail.getFromLocationId() : transferDetail.getToLocationId(), scanLocationId);
        if (!check) {
            ToastTool.show(this, "库位不一致");
        } else {
            mLocationView.setVisibility(View.GONE);
            mProductView.setVisibility(View.VISIBLE);
            mProductLocationView.setText("库位：" + mLocationIdView.getText().toString());
            if (!isTransferFrom) {
                button.setEnabled(true);
                button.setClickable(true);
            }
        }
    }

    @Override
    public void onInputError(int i) {
        button.setEnabled(false);
        button.setClickable(false);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    private class RequestTransferTask extends HttpAsyncTask<ResponseState> {

        private String locationId;
        private String qty;
        private String packName;
        private String taskId;
        private boolean isTransferFrom;

        public RequestTransferTask(Context context, String taskId, String locationId, String qty, String packName, boolean isTransferFrom) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.qty = qty;
            this.packName = packName;
            this.taskId = taskId;
            this.isTransferFrom = isTransferFrom;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            if (isTransferFrom) {
                return HttpApi.procurementScanFromLocation(taskId, locationId, BaseApplication.get().getUserId(), qty, packName);
            } else {
                return HttpApi.procurementScanToLocation(taskId, locationId, BaseApplication.get().getUserId(), qty, packName);
            }
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            ToastTool.show(context, isTransferFrom ? "转出成功" : "转入成功");
            if (isTransferFrom) {
                transferDetail.setUomQty(qty);
                ProcurementActivity.launch(context, transferDetail, false);
                finish();
            } else {
                finish();
            }
        }
    }


}
