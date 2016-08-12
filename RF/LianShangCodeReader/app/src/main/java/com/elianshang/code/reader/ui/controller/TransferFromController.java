package com.elianshang.code.reader.ui.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/12.
 */
public class TransferFromController extends BaseTransferController {

    public TransferFromController(Activity activity) {
        super(activity);
    }

    @Override
    protected void onSubmitClick() {
        String qty = mItemQtyRealView.getValue();
        String locationId = detail.getFromLocationId();
        if (TextUtils.isEmpty(qty) || TextUtils.isEmpty(taskId) || TextUtils.isEmpty(locationId)) {
            return;
        }
        new RequestTransferTask(activity, taskId, locationId, qty).start();
    }

    @Override
    protected String getLocationId() {
        return detail.getFromLocationId();
    }

    @Override
    public void fillLocationLayout() {
        mLocationView.setVisibility(View.VISIBLE);
        mItemView.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);

        mTaskView.setText("任务：" + taskId);
        mTypeNameView.setText("开始移库转出");
        mLocationIdView.setText(detail.getFromLocationName());
        mLocationIdConfirmView.getText().clear();


    }

    @Override
    protected void onLocationConfirmSuccess() {
        Log.e("lhz","From:  onLocationConfirmSuccess()");
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.VISIBLE);
        mItemQtyRealView.requestFocus();

        mTypeNameView.setText("填写转出数量");
        mItemNameView.setText("商品名称：" + detail.getProductName());
        mItemPackNameView.setText("包装单位：" + detail.getProductPackName());
        mItemQtyView.setText("商品数量：" + detail.getUomQty());
        mItemLocationView.setText("库位：" + detail.getFromLocationName());

    }

    @Override
    protected void onLocationConfirmFailed() {
        Log.e("lhz","From:  onLocationConfirmFailed()");
        ToastTool.show(activity, "库位不一致");
    }

    private class RequestTransferTask extends HttpAsyncTask<ResponseState> {

        private String locationId;
        private String qty;
        private String taskId;

        public RequestTransferTask(Context context, String taskId, String locationId, String qty) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.qty = qty;
            this.taskId = taskId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.stockTransferScanFromLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            if (mTransferCompleteListener != null) {
                mTransferCompleteListener.onTransferSuccess();
            }
        }
    }

}
