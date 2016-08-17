package com.elianshang.code.reader.ui.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.view.ProcurementView;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class ProcurementFromController extends BaseProcurementController {

    public ProcurementFromController(Activity activity, String taskId, ProcurementView procurementView) {
        super(activity, taskId, procurementView);
        requestDetailTask();
    }

    @Override
    public void onSubmitClick(String qty) {
        String locationId = detail.getFromLocationId();
        if (TextUtils.isEmpty(qty) || TextUtils.isEmpty(taskId) || TextUtils.isEmpty(locationId)) {
            return;
        }
        new RequestTransferTask(activity, taskId, locationId, qty).start();

    }

    @Override
    public void onComplete(String s) {
        boolean check = TextUtils.equals(detail.getFromLocationId(), s);
        if (!check) {
            ToastTool.show(activity, "库位不一致");
        } else {
            if (procurementView != null) {
                procurementView.showItemView("填写转出数量", "商品名称：" + detail.getProductName(), "商品名称：" + detail.getProductPackName(), "商品数量：" + detail.getUomQty(), "库位：" + detail.getFromLocationName());
            }
        }
    }

    private void fillConfirmLocationView() {
        if (procurementView != null) {
            procurementView.showLocationConfirmView("开始补货转出", "任务：" + taskId, detail.getFromLocationName());
        }
    }

    protected void requestDetailTask() {
        new FetchProcurementDetailTask(activity, taskId).start();
    }

    /**
     * Created by liuhanzhi on 16/8/3. 领取补货任务
     */
    private class FetchProcurementDetailTask extends HttpAsyncTask<TaskTransferDetail> {

        private String taskId;

        public FetchProcurementDetailTask(Context context, String taskId) {
            super(context, true, true, false);
            this.taskId = taskId;
        }

        @Override
        public DataHull<TaskTransferDetail> doInBackground() {
            return HttpApi.procurementView(taskId);
        }

        @Override
        public void onPostExecute(int updateId, TaskTransferDetail result) {
            detail = result;
            fillConfirmLocationView();
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            ToastTool.show(context, "没有补货任务");
        }

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
            return HttpApi.procurementScanFromLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            if (mTransferCompleteListener != null) {
                mTransferCompleteListener.onTransferSuccess();
            }
        }
    }

}
