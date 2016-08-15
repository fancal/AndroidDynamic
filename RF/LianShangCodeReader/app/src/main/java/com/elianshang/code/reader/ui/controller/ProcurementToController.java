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
public class ProcurementToController extends BaseProcurementController {

    public ProcurementToController(Activity activity, String taskId, ProcurementView procurementView , TaskTransferDetail detail) {
        super(activity, taskId, procurementView);
        this.detail = detail;
        fillConfirmLocationView();
    }

    @Override
    public void onSubmitClick(String qty) {

    }

    @Override
    public void onComplete(String s) {
        boolean check = TextUtils.equals(detail.getToLocationId(), s);
        if (!check) {
            ToastTool.show(activity, "库位不一致");
        } else {
            submit();
        }
    }

    private void submit() {
        String locationId = detail.getFromLocationId();
        if (TextUtils.isEmpty(taskId) || TextUtils.isEmpty(locationId)) {
            return;
        }
        new RequestTransferTask(activity, taskId, locationId, "").start();

    }

    private void fillConfirmLocationView() {
        if (procurementView != null) {
            procurementView.showLocationConfirmView("转入到库位", "任务："+taskId, detail.getToLocationName());
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
            return HttpApi.procurementScanToLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            if (mTransferCompleteListener != null) {
                mTransferCompleteListener.onTransferSuccess();
            }
        }
    }

}
