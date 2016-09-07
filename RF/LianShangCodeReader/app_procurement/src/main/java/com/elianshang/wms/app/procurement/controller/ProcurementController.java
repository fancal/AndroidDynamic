package com.elianshang.wms.app.procurement.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.bean.ProcurementNext;
import com.elianshang.wms.app.procurement.provider.ScanLocationProvider;
import com.elianshang.wms.app.procurement.view.ProcurementView;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class ProcurementController extends BaseProcurementController implements BaseProcurementController.TransferCompleteListener {

    private String serialNumber;

    public ProcurementController(Activity activity, String uId, String uToken, Procurement procurement, ProcurementView procurementView) {
        super(activity, uId, uToken, procurement, procurementView);
        fillData();
    }

    public void fillData() {
        if (curProcurement != null) {
            serialNumber = DeviceTool.generateSerialNumber(activity, getClass().getName());
            if (TextUtils.equals("2", curProcurement.getType())) {
                fillInBound();
            } else if (TextUtils.equals("1", curProcurement.getType())) {
                fillOutBound();
            }
        }
    }

    private void fillInBound() {
        if (procurementView != null) {
            procurementView.showLocationConfirmView(true, "转入到库位", "任务：" + curProcurement.getTaskId(), "商品名称：" + curProcurement.getItemName(), "商品规格：" + curProcurement.getPackName(), "商品数量：" + curProcurement.getUomQty(), curProcurement.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (procurementView != null) {
            procurementView.showLocationConfirmView(false, "转入到库位", "任务：" + curProcurement.getTaskId(), "商品名称：" + curProcurement.getItemName(), "商品规格：" + curProcurement.getPackName(), "商品数量：" + curProcurement.getUomQty(), curProcurement.getLocationCode());
        }
    }

    @Override
    public void onSubmitClick(String qty) {
        if (curProcurement != null) {
            if (TextUtils.equals("1", curProcurement.getType())) {
                String numQty = "1".equals(curProcurement.getSubType()) ? curProcurement.getUomQty() : qty;
                submit(numQty);
            }
        }
    }

    @Override
    public void onComplete(String s) {
        if (curProcurement != null) {
            boolean check = TextUtils.equals(curProcurement.getLocationId(), s);
            if (!check) {
                ToastTool.show(activity, "库位不一致");
            } else {
                if (TextUtils.equals("2", curProcurement.getType())) {
                    submit(curProcurement.getUomQty());
                } else if (TextUtils.equals("1", curProcurement.getType())) {
                    if (procurementView != null) {
                        String numQty = "1".equals(curProcurement.getSubType()) ? null : curProcurement.getUomQty();
                        procurementView.showItemView("填写转出数量", "商品名称：" + curProcurement.getItemName(), "商品名称：" + curProcurement.getPackName(), "商品数量：" + curProcurement.getUomQty(), "库位：" + curProcurement.getLocationCode(), numQty);
                    }
                }
            }
        }
    }

    private void submit(String qty) {
        new ScanLocationTask(activity, uId, uToken, curProcurement.getType(), curProcurement.getTaskId(), curProcurement.getLocationId(), qty).start();
    }


    @Override
    public void onTransferSuccess() {
        ToastTool.show(activity, "补货任务完成");
        activity.finish();
    }

    private class ScanLocationTask extends HttpAsyncTask<ProcurementNext> {

        private String uId;

        private String uToken;

        private String type;

        private String taskId;

        private String locationId;

        private String qty;

        public ScanLocationTask(Context context, String uId, String uToken, String type, String taskId, String locationId, String qty) {
            super(context, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
            this.type = type;
            this.locationId = locationId;
            this.qty = qty;
            this.taskId = taskId;
        }

        @Override
        public DataHull<ProcurementNext> doInBackground() {
            return ScanLocationProvider.request(context, uId, uToken, type, taskId, locationId, qty, serialNumber);
        }

        @Override
        public void onPostExecute(ProcurementNext result) {
            if (result.isDone()) {
                onTransferSuccess();
            } else {
                curProcurement = result.getProcurement();
                fillData();
            }
        }
    }
}
