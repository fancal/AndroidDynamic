package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.TaskTransfer;
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
public class ProcurementActivity extends BaseActivity implements ScanEditTextTool.OnSetComplete, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        new FetchProcurementTask(context).start();
    }

    private static void launchInner(Context context, TaskTransferDetail detail, boolean isFrom) {
        Intent intent = new Intent(context, ProcurementActivity.class);
        intent.putExtra("detail", detail);
        intent.putExtra("isFrom", isFrom);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    /**
     * taskId
     */
    private TextView mTaskView;
    /**
     * 商品名称
     */
    private TextView mItemNameView;
    /**
     * 包装单位
     */
    private TextView mItemPackNameView;
    /**
     * 商品数量
     */
    private TextView mItemQtyView;
    /**
     * 实际数量
     */
    private EditText mItemQtyRealView;
    /**
     * 实际数量  container
     */
    private View mItemQtyRealContainerView;
    /**
     * 库位Id
     */
    private TextView mLocationIdView;
    /**
     * 确认库位Id
     */
    private ScanEditText mLocationIdConfirmView;
    /**
     * 转入/转出
     */
    private TextView mTypeNameView;
    private ScanEditTextTool scanEditTextTool;
    /**
     * 提交
     */
    private Button mSubmit;
    /**
     * 商品container
     */
    private View mItemView;
    /**
     * 库位container
     */
    private View mLocationView;
    /**
     * 商品库位
     */
    private TextView mItemLocationView;

    private TaskTransferDetail transferDetail;
    /**
     * from 转出
     */
    private boolean isTransferFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement);
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
        isTransferFrom = getIntent().getBooleanExtra("isFrom", false);
        transferDetail = (TaskTransferDetail) getIntent().getSerializableExtra("detail");
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
        mItemNameView = (TextView) findViewById(R.id.item_name);
        mItemPackNameView = (TextView) findViewById(R.id.item_pack_name);
        mItemQtyView = (TextView) findViewById(R.id.item_qty);
        mItemQtyRealView = (EditText) findViewById(R.id.item_qty_real);
        mItemQtyRealContainerView = findViewById(R.id.item_qty_real_container);
        mLocationIdView = (TextView) findViewById(R.id.location_id);
        mLocationIdConfirmView = (ScanEditText) findViewById(R.id.confirm_location_id);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mItemView = findViewById(R.id.item);
        mLocationView = findViewById(R.id.location);
        mItemLocationView = (TextView) findViewById(R.id.item_locationId);

        mTypeNameView.setText(isTransferFrom ? "转出" : "转入");
        scanEditTextTool = new ScanEditTextTool(this, mLocationIdConfirmView);
        scanEditTextTool.setComplete(this);

        mSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            submit();
        }
    }

    private void submit() {
        String qty = isTransferFrom ? mItemQtyRealView.getText().toString() : transferDetail.getUomQty();
        String taskId = transferDetail.getTaskId();
        String locationId = isTransferFrom ? transferDetail.getFromLocationId() : transferDetail.getToLocationId();
        if (TextUtils.isEmpty(qty) || TextUtils.isEmpty(taskId) || TextUtils.isEmpty(locationId)) {
            return;
        }
        new RequestTransferTask(ProcurementActivity.this, taskId, locationId, qty, isTransferFrom).start();

    }


    private void fill() {
        mTaskView.setText("任务ID：" + transferDetail.getTaskId());
        mItemNameView.setText("商品名称：" + transferDetail.getProductName());
        mItemPackNameView.setText("包装单位：" + transferDetail.getProductPackName());
        mItemQtyView.setText((isTransferFrom ? "商品数量：" : "实际数量：") + transferDetail.getUomQty());
        mLocationIdView.setText(isTransferFrom ? transferDetail.getFromLocationName() : transferDetail.getToLocationName());
        mItemQtyRealContainerView.setVisibility(isTransferFrom ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSetComplete() {
        String scanLocationId = mLocationIdConfirmView.getText().toString();
        boolean check = TextUtils.equals(isTransferFrom ? transferDetail.getFromLocationId() : transferDetail.getToLocationId(), scanLocationId);
        if (!check) {
            ToastTool.show(this, "库位不一致");
        } else {
            mLocationView.setVisibility(View.GONE);
            mItemView.setVisibility(View.VISIBLE);
            mItemLocationView.setText("库位：" + mLocationIdView.getText().toString());
            if (!isTransferFrom) {
                mSubmit.setEnabled(true);
                mSubmit.setClickable(true);
            }
        }
    }

    @Override
    public void onInputError(int i) {
        mSubmit.setEnabled(false);
        mSubmit.setClickable(false);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    private class RequestTransferTask extends HttpAsyncTask<ResponseState> {

        private String locationId;
        private String qty;
        private String taskId;
        private boolean isFrom;

        public RequestTransferTask(Context context, String taskId, String locationId, String qty, boolean isFrom) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.qty = qty;
            this.taskId = taskId;
            this.isFrom = isFrom;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            if (isFrom) {
                return HttpApi.procurementScanFromLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
            } else {
                return HttpApi.procurementScanToLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
            }
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            ToastTool.show(context, isFrom ? "转出成功" : "转入成功");
            if (isFrom) {
                transferDetail.setUomQty(qty);
                ProcurementActivity.launchInner(context, transferDetail, false);
                finish();
            } else {
                finish();
            }
        }
    }

    /**
     * Created by liuhanzhi on 16/8/3. 领取补货任务
     */
    private static class FetchProcurementTask extends HttpAsyncTask<TaskTransferDetail> {

        public FetchProcurementTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<TaskTransferDetail> doInBackground() {
            DataHull<TaskTransfer> dataHull = HttpApi.procurementFetchTask("", BaseApplication.get().getUserId());
            DataHull<TaskTransferDetail> dataHull1;
            if (dataHull != null && dataHull.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                TaskTransfer taskTransfer = dataHull.getDataEntity();
                dataHull1 = HttpApi.procurementView(taskTransfer.getTaskId());
                if (dataHull1.getDataEntity() != null) {
                    dataHull1.getDataEntity().setTaskId(taskTransfer.getTaskId());
                }
            } else {
                dataHull1 = new DataHull<>();
                dataHull1.setStatus(dataHull.getStatus());
                dataHull1.setMessage(dataHull.getMessage());
            }
            return dataHull1;
        }

        @Override
        public void onPostExecute(int updateId, TaskTransferDetail result) {
            ProcurementActivity.launchInner(context, result, true);
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            ToastTool.show(context, "没有补货任务");
        }

    }


}
