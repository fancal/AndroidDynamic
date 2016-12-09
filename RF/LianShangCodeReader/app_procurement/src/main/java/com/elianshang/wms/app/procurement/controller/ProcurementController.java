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
            procurementView.showLocationConfirmView(
                    true,
                    "转入到库位",
                    "任务：" + curProcurement.getTaskId(),
                    "名称：" + curProcurement.getItemName(),
                    "国条码：" + curProcurement.getBarcode(),
                    "物美码：" + curProcurement.getSkuCode(),
                    "规格：" + curProcurement.getPackName(),
                    "数量：" + curProcurement.getQty(),
                    curProcurement.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (procurementView != null) {
            procurementView.showLocationConfirmView(
                    false,
                    "开始补货转出",
                    "任务：" + curProcurement.getTaskId(),
                    "名称：" + curProcurement.getItemName(),
                    "国条码：" + curProcurement.getBarcode(),
                    "物美码：" + curProcurement.getSkuCode(),
                    "规格：" + curProcurement.getPackName(),
                    "数量：" + curProcurement.getQty(),
                    curProcurement.getLocationCode());
        }
    }

    @Override
    public void onSubmitClick(String qty, String scatterQty) {

        if (curProcurement != null) {
            if (TextUtils.equals("1", curProcurement.getType())) {
                if (!"1".equals(curProcurement.getSubType()) && TextUtils.isEmpty(qty) && TextUtils.isEmpty(scatterQty)) {
                    ToastTool.show(activity, "请输入正确的数量");
                    return;
                }

                String numQty = "1".equals(curProcurement.getSubType()) ? "0" : qty;
                String sscatterQty = "1".equals(curProcurement.getSubType()) ? "0" : scatterQty;
                submit(numQty, sscatterQty);
            }
        }
    }

    @Override
    public void onComplete(String s) {
        if (curProcurement != null) {
            boolean check = TextUtils.equals(curProcurement.getLocationCode(), s);
            if (!check) {
                ToastTool.show(activity, "库位不一致");
            } else {
                if (TextUtils.equals("2", curProcurement.getType())) {
                    submit(curProcurement.getQty() , "0");
                } else if (TextUtils.equals("1", curProcurement.getType())) {
                    if (procurementView != null) {
                        String numQty = "1".equals(curProcurement.getSubType()) ? null : curProcurement.getQty();
                        procurementView.showItemView(
                                "填写转出数量",
                                "名称：" + curProcurement.getItemName(),
                                "国条码：" + curProcurement.getBarcode(),
                                "物美码：" + curProcurement.getSkuCode(),
                                "规格：" + curProcurement.getPackName(),
                                "数量：" + curProcurement.getQty(),
                                "库位：" + curProcurement.getLocationCode(),
                                numQty);
                    }
                }
            }
        }
    }

    private void submit(String qty, String scatterQty) {
        new ScanLocationTask(activity, uId, uToken, curProcurement.getType(), curProcurement.getTaskId(), curProcurement.getLocationCode(), qty, scatterQty).start();
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

        private String locationCode;

        private String qty;

        private String scatterQty;

        public ScanLocationTask(Context context, String uId, String uToken, String type, String taskId, String locationCode, String qty, String scatterQty) {
            super(context, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
            this.type = type;
            this.locationCode = locationCode;
            this.qty = qty;
            this.taskId = taskId;
            this.scatterQty = scatterQty;
        }

        @Override
        public DataHull<ProcurementNext> doInBackground() {
            return ScanLocationProvider.request(context, uId, uToken, type, taskId, locationCode, qty, scatterQty, serialNumber);
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
