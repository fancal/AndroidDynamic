package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.TaskTransfer;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.controller.ProcurementController;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ProcurementView;
import com.elianshang.code.reader.ui.view.QtyEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 补货
 */
public class ProcurementActivity extends BaseActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, ProcurementView {

    public static void launch(Context context) {
        new FetchProcurementTask(context).start();
    }

    private static void launchInner(Context context, String taskId) {
        Intent intent = new Intent(context, ProcurementActivity.class);
        intent.putExtra("taskId", taskId);
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
    private QtyEditText mItemQtyRealView;
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

    private String taskId;

    private ProcurementController procurementController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement);
        readExtras();
        findViews();

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
        taskId = getIntent().getStringExtra("taskId");
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
        mItemQtyRealView = (QtyEditText) findViewById(R.id.item_qty_real);
        mItemQtyRealContainerView = findViewById(R.id.item_qty_real_container);
        mLocationIdView = (TextView) findViewById(R.id.location_id);
        mLocationIdConfirmView = (ScanEditText) findViewById(R.id.confirm_location_id);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mItemView = findViewById(R.id.item);
        mLocationView = findViewById(R.id.location);
        mItemLocationView = (TextView) findViewById(R.id.item_locationId);

        scanEditTextTool = new ScanEditTextTool(this, mLocationIdConfirmView);
        scanEditTextTool.setComplete(this);

        mSubmit.setOnClickListener(this);
        mSubmit.setVisibility(View.GONE);

        procurementController = new ProcurementController(this, taskId, this);

    }

    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            procurementController.onSubmitClick(mItemQtyRealView.getValue());
        }
    }

    @Override
    public void onComplete() {
        procurementController.onComplete(mLocationIdConfirmView.getText().toString());
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void showLocationConfirmView(String typeName, String taskId, String locationName) {
        mLocationView.setVisibility(View.VISIBLE);
        mItemView.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);

        mTaskView.setText(taskId);
        mTypeNameView.setText(typeName);
        mLocationIdView.setText(locationName);
        mLocationIdConfirmView.getText().clear();
    }

    @Override
    public void showItemView(String typeName, String itemName, String packName, String qty, String locationName) {
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.VISIBLE);
        mItemQtyRealView.requestFocus();

        mTypeNameView.setText(typeName);
        mItemNameView.setText(itemName);
        mItemPackNameView.setText(packName);
        mItemQtyView.setText(qty);
        mItemLocationView.setText(locationName);
    }


    /**
     * Created by liuhanzhi on 16/8/3. 领取补货任务
     */
    private static class FetchProcurementTask extends HttpAsyncTask<TaskTransfer> {

        public FetchProcurementTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<TaskTransfer> doInBackground() {
            return HttpApi.procurementFetchTask("", BaseApplication.get().getUserId());
        }

        @Override
        public void onPostExecute(int updateId, TaskTransfer result) {
            ProcurementActivity.launchInner(context, result.getTaskId());
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            ToastTool.show(context, "没有补货任务");
        }

    }


}
