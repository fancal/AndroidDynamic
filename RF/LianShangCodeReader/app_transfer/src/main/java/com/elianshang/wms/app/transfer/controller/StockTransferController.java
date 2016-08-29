package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.bean.TransferNext;
import com.elianshang.wms.app.transfer.provider.ScanLocationProvider;
import com.elianshang.wms.app.transfer.view.StockTransferView;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class StockTransferController extends BaseStockTransferController implements BaseStockTransferController.TransferCompleteListener {

    public StockTransferController(Activity activity, String uId, String uToken, Transfer transfer, StockTransferView stockTransferView) {
        super(activity, uId, uToken, transfer, stockTransferView);
        fillData();
    }

    public void fillData() {
        if (curTransfer != null) {
            if (TextUtils.equals("2", curTransfer.getType())) {
                fillInBound();
            } else if (TextUtils.equals("1", curTransfer.getType())) {
                fillOutBound();
            }
        }
    }

    private void fillInBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView("转入到库位", "任务：" + curTransfer.getTaskId(), curTransfer.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView("开始移库转出", "任务：" + curTransfer.getTaskId(), curTransfer.getLocationCode());
        }
    }

    @Override
    public void onSubmitClick(String qty) {
        if (curTransfer != null) {
            if (TextUtils.equals("1", curTransfer.getType())) {
                String numQty = "1".equals(curTransfer.getSubType()) ? curTransfer.getUomQty() : qty;
                submit(numQty);
            }
        }
    }

    @Override
    public void onComplete(String s) {
        if (curTransfer != null) {
            boolean check = TextUtils.equals(curTransfer.getLocationId(), s);
            if (!check) {
                ToastTool.show(activity, "库位不一致");
            } else {
                if (TextUtils.equals("2", curTransfer.getType())) {
                    submit(curTransfer.getUomQty());
                } else if (TextUtils.equals("1", curTransfer.getType())) {
                    if (stockTransferView != null) {
                        String numQty = "1".equals(curTransfer.getSubType()) ? null : curTransfer.getUomQty();
                        stockTransferView.showItemView("填写转出数量", "商品名称：" + curTransfer.getItemName(), "商品名称：" + curTransfer.getPackName(), "商品数量：" + curTransfer.getUomQty(), "库位：" + curTransfer.getLocationCode(), numQty);
                    }
                }
            }
        }
    }

    private void submit(String qty) {
        new ScanLocationTask(activity, uId, uToken, curTransfer.getType(), curTransfer.getTaskId(), curTransfer.getLocationId(), qty).start();
    }


    @Override
    public void onTransferSuccess() {
        ToastTool.show(activity, "移库任务完成");
        activity.finish();
    }

    private class ScanLocationTask extends HttpAsyncTask<TransferNext> {

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
        public DataHull<TransferNext> doInBackground() {
            return ScanLocationProvider.request(context, uId, uToken, type, taskId, locationId, qty);
        }

        @Override
        public void onPostExecute(TransferNext result) {
            if (result.isDone()) {
                onTransferSuccess();
            } else {
                curTransfer = result.getTransfer();
                fillData();
            }
        }
    }
}
