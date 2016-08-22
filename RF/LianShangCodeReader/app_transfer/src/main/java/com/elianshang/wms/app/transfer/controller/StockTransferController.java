package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.transfer.bean.StockTransfer;
import com.elianshang.wms.app.transfer.bean.StockTransferNext;
import com.elianshang.wms.app.transfer.provider.ScanLocationProvider;
import com.elianshang.wms.app.transfer.ui.view.StockTransferView;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class StockTransferController extends BaseStockTransferController implements BaseStockTransferController.TransferCompleteListener {

    public StockTransferController(Activity activity, String uId, String uToken, StockTransfer stockTransfer, StockTransferView stockTransferView) {
        super(activity, uId, uToken, stockTransfer, stockTransferView);
        fillData();
    }

    public void fillData() {
        if (curStockTransfer != null) {
            if (TextUtils.equals("2", curStockTransfer.getType())) {
                fillInBound();
            } else if (TextUtils.equals("1", curStockTransfer.getType())) {
                fillOutBound();
            }
        }
    }

    private void fillInBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView("转入到库位", "任务：" + curStockTransfer.getTaskId(), curStockTransfer.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView("开始移库转出", "任务：" + curStockTransfer.getTaskId(), curStockTransfer.getLocationCode());
        }
    }

    @Override
    public void onSubmitClick(String qty) {
        if (curStockTransfer != null) {
            if (TextUtils.equals("1", curStockTransfer.getType())) {
                submit(qty);
            }
        }
    }

    @Override
    public void onComplete(String s) {
        if (curStockTransfer != null) {
            boolean check = TextUtils.equals(curStockTransfer.getLocationId(), s);
            if (!check) {
                ToastTool.show(activity, "库位不一致");
            } else {
                if (TextUtils.equals("2", curStockTransfer.getType())) {
                    submit(curStockTransfer.getUomQty());
                } else if (TextUtils.equals("1", curStockTransfer.getType())) {
                    if (stockTransferView != null) {
                        stockTransferView.showItemView("填写转出数量", "商品名称：" + curStockTransfer.getItemName(), "商品名称：" + curStockTransfer.getPackName(), "商品数量：" + curStockTransfer.getUomQty(), "库位：" + curStockTransfer.getLocationCode());
                    }
                }
            }
        }
    }

    private void submit(String qty) {
        new ScanLocationTask(activity, uId, uToken, curStockTransfer.getType(), curStockTransfer.getTaskId(), curStockTransfer.getLocationId(), qty).start();
    }


    @Override
    public void onTransferSuccess() {
        ToastTool.show(activity, "移库任务完成");
        activity.finish();
    }

    private class ScanLocationTask extends HttpAsyncTask<StockTransferNext> {

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
        public DataHull<StockTransferNext> doInBackground() {
            return ScanLocationProvider.request(uId, uToken, type, taskId, locationId, qty);
        }

        @Override
        public void onPostExecute(int updateId, StockTransferNext result) {
            if (result.isDone()) {
                onTransferSuccess();
            } else {
                curStockTransfer = result.getStockTransfer();
                fillData();
            }
        }
    }
}
