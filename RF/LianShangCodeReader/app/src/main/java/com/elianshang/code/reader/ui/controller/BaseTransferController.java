package com.elianshang.code.reader.ui.controller;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.QtyEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;

/**
 * Created by liuhanzhi on 16/8/12.
 */
public abstract class BaseTransferController implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    protected Activity activity;

    protected Toolbar mToolbar;
    /**
     * 任务Id
     */
    protected TextView mTaskView;
    /**
     * 商品名称
     */
    protected TextView mItemNameView;
    /**
     * 包装单位
     */
    protected TextView mItemPackNameView;
    /**
     * 商品数量
     */
    protected TextView mItemQtyView;
    /**
     * 实际数量
     */
    protected QtyEditText mItemQtyRealView;
    /**
     * 实际数量  容器View
     */
    protected View mItemQtyRealContainerView;
    /**
     * 库位码
     */
    protected TextView mLocationIdView;
    /**
     * 确认库位码
     */
    protected ScanEditText mLocationIdConfirmView;
    /**
     * 转入/转出
     */
    protected TextView mTypeNameView;
    protected ScanEditTextTool scanEditTextTool;
    /**
     * 提交
     */
    protected Button mSubmit;
    /**
     * 商品container
     */
    protected View mItemView;
    /**
     * 库位container
     */
    protected View mLocationView;
    /**
     * 库位
     */
    protected TextView mItemLocationView;

    protected TransferCompleteListener mTransferCompleteListener;

    protected TaskTransferDetail detail;

    protected String taskId;

    public BaseTransferController(Activity activity) {
        this.activity = activity;
        findViews();
    }

    private void findViews() {
        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        mTaskView = (TextView) activity.findViewById(R.id.task_id);
        mItemNameView = (TextView) activity.findViewById(R.id.item_name);
        mItemPackNameView = (TextView) activity.findViewById(R.id.item_pack_name);
        mItemQtyView = (TextView) activity.findViewById(R.id.item_qty);
        mItemQtyRealView = (QtyEditText) activity.findViewById(R.id.item_qty_real);
        mItemQtyRealContainerView = activity.findViewById(R.id.item_qty_real_container);
        mLocationIdView = (TextView) activity.findViewById(R.id.location_id);
        mLocationIdConfirmView = (ScanEditText) activity.findViewById(R.id.confirm_location_id);
        mSubmit = (Button) activity.findViewById(R.id.submit_button);
        mTypeNameView = (TextView) activity.findViewById(R.id.transfer_type_name);
        mItemView = activity.findViewById(R.id.item);
        mLocationView = activity.findViewById(R.id.location);
        mItemLocationView = (TextView) activity.findViewById(R.id.item_locationId);

        scanEditTextTool = new ScanEditTextTool(activity, mLocationIdConfirmView);
        scanEditTextTool.setComplete(this);

        mSubmit.setOnClickListener(this);
        mSubmit.setVisibility(View.GONE);

    }

    public void setData(TaskTransferDetail detail, String taskId) {
        this.detail = detail;
        this.taskId = taskId;
    }

    public void release() {
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
    }

    protected abstract void onSubmitClick();

    protected abstract String getLocationId();

    public abstract void fillLocationLayout();

    protected abstract void onLocationConfirmSuccess();

    protected abstract void onLocationConfirmFailed();

    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            onSubmitClick();
        }
    }

    @Override
    public void onComplete() {
        Log.e("lhz", "onComplete");
        if (TextUtils.equals(getLocationId(), mLocationIdConfirmView.getText().toString())) {
            onLocationConfirmSuccess();
        } else {
            onLocationConfirmFailed();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void OnBarCodeReceived(String s) {
        if(scanEditTextTool != null){
            scanEditTextTool.setScanText(s);
        }
    }

    public TransferCompleteListener getTransferCompleteListener() {
        return mTransferCompleteListener;
    }

    public void setTransferCompleteListener(TransferCompleteListener mTransferCompleteListener) {
        this.mTransferCompleteListener = mTransferCompleteListener;
    }

    public interface TransferCompleteListener {
        void onTransferSuccess();
    }
}
