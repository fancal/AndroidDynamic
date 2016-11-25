package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.transfer.bean.LocationView;
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.bean.TransferNext;
import com.elianshang.wms.app.transfer.provider.ScanLocationProvider;
import com.elianshang.wms.app.transfer.provider.ViewLocationProvider;
import com.elianshang.wms.app.transfer.view.StockTransferView;
import com.xue.http.impl.DataHull;

public class StockTransferController extends BaseStockTransferController implements BaseStockTransferController.TransferCompleteListener {

    private String serialNumber;

    public StockTransferController(Activity activity, String uId, String uToken, Transfer transfer, StockTransferView stockTransferView) {
        super(activity, uId, uToken, transfer, stockTransferView);
        fillData();
    }

    public void fillData() {
        serialNumber = DeviceTool.generateSerialNumber(activity, getClass().getName());
        if (curTransfer != null) {
            if (TextUtils.equals("2", curTransfer.getType())) {
                fillInBound();
            } else if (TextUtils.equals("1", curTransfer.getType())) {
                fillOutBound();
            }
        } else {
            stockTransferView.showScanLayout();
        }
    }

    private void fillInBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView(true, "转入到库位", TextUtils.isEmpty(curTransfer.getTaskId()) ? null : "任务：" + curTransfer.getTaskId(), "名称：" + curTransfer.getItemName(), "规格：" + curTransfer.getPackName(), "数量：" + curTransfer.getUomQty(), curTransfer.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView(false, "开始移库转出", TextUtils.isEmpty(curTransfer.getTaskId()) ? null : "任务：" + curTransfer.getTaskId(), "名称：" + curTransfer.getItemName(), "规格：" + curTransfer.getPackName(), "数量：" + curTransfer.getUomQty(), curTransfer.getLocationCode());
        }
    }

    public boolean onBackPressed() {
        if (curTransfer == null) {
            activity.finish();
            return true;
        } else {
            if("0".equals(curTransfer.getTaskId())){
                curTransfer = null ;
                stockTransferView.showScanLayout();
                return true ;
            }
        }
        return false;
    }

    @Override
    public void onSubmitClick(String qty) {
        if (curTransfer != null) {
            if (TextUtils.equals("1", curTransfer.getType())) {
                String subType = null;
                if ("-1".equals(qty)) {
                    subType = "1";
                    qty = "1";
                }
                String numQty = "1".equals(curTransfer.getSubType()) ? curTransfer.getUomQty() : qty;
                submit(subType, numQty);
            }
        }
    }

    public void onScanComplete(String s) {
        if (!TextUtils.isEmpty(s)) {
            new ViewLocationTask(activity, uId, uToken, s).start();
        }
    }

    public void onWorkComplete(final String s) {
        if (curTransfer != null) {
            boolean check = true;
            if (!TextUtils.isEmpty(curTransfer.getLocationCode())) {
                check = TextUtils.equals(curTransfer.getLocationCode(), s);
            }

            if (TextUtils.equals("1", curTransfer.getType())) {
                if (!check) {
                    ToastTool.show(activity, "库位不一致");
                } else {
                    if (stockTransferView != null) {
                        String numQty = curTransfer.getUomQty();
                        if ("1".equals(curTransfer.getSubType())) {
                            numQty = null;
                        } else if ("-1".equals(curTransfer.getSubType())) {
                            numQty = "";
                        }
                        stockTransferView.showItemView("填写转出数量", "名称：" + curTransfer.getItemName(), "规格：" + curTransfer.getPackName(), "数量：" + curTransfer.getUomQty(), "库位：" + curTransfer.getLocationCode(), numQty);
                    }
                }
            } else if (TextUtils.equals("2", curTransfer.getType())) {
                if (!check) {
                    DialogTools.showTwoButtonDialog(activity, "扫描的库位与推荐的库位不一致，确认移入", "确认", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            submit(s);
                        }
                    }, null, true);
                } else {
                    submit(s);
                }
            }
        }
    }

    /**
     * 提交移出
     */
    private void submit(String subType, String qty) {
        new ScanLocationTask(activity, uId, uToken, curTransfer.getType(), curTransfer.getTaskId(), curTransfer.getLocationCode(), curTransfer.getBarCode(), curTransfer.getUom(), qty, subType).start();
    }

    /**
     * 提交移入
     */
    private void submit(String locationCode) {
        new ScanLocationTask(activity, uId, uToken, curTransfer.getType(), curTransfer.getTaskId(), locationCode, curTransfer.getBarCode(), null, null, null).start();
    }


    @Override
    public void onTransferSuccess() {
        ToastTool.show(activity, "移库任务完成");
        activity.finish();
    }

    /**
     * 查询库位信息
     */
    private class ViewLocationTask extends HttpAsyncTask<LocationView> {

        private String uId;

        private String uToken;

        private String locationCode;

        public ViewLocationTask(Context context, String uId, String uToken, String locationCode) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<LocationView> doInBackground() {
            return ViewLocationProvider.request(context, uId, uToken, locationCode);
        }

        @Override
        public void onPostExecute(LocationView result) {
            Transfer transfer = new Transfer();
            transfer.setTaskId("0");
            transfer.setItemId(result.getItemId());
            transfer.setType("1");
            transfer.setSubType("-1");
            transfer.setItemName(result.getItemName());
            transfer.setLocationCode(result.getLocationCode());
            transfer.setPackName(result.getPackName());
            transfer.setUomQty(result.getUomQty());
            transfer.setBarCode(result.getBarCode());
            transfer.setUom(result.getUom());

            curTransfer = transfer;
            fillData();
            onWorkComplete(locationCode);
        }
    }

    private class ScanLocationTask extends HttpAsyncTask<TransferNext> {

        private String uId;

        private String uToken;

        private String type;

        private String taskId;

        private String locationCode;

        private String barCode;

        private String uom;

        private String qty;

        private String subType;

        public ScanLocationTask(Context context, String uId, String uToken, String type, String taskId, String locationCode, String barCode, String uom, String qty, String subType) {
            super(context, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
            this.type = type;
            this.locationCode = locationCode;
            this.barCode = barCode;
            this.uom = uom;
            this.qty = qty;
            this.taskId = taskId;
            this.subType = subType;
        }

        @Override
        public DataHull<TransferNext> doInBackground() {
            return ScanLocationProvider.request(context, uId, uToken, type, taskId, locationCode, barCode, uom, qty, subType, serialNumber);
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
