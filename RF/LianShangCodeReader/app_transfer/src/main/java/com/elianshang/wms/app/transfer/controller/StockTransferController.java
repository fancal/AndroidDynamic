package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

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
            stockTransferView.showLocationConfirmView(
                    true,
                    "转入到库位",
                    TextUtils.isEmpty(curTransfer.getTaskId()) ? null : "任务：" + curTransfer.getTaskId(),
                    "名称：" + curTransfer.getItemName(),
                    "国条码：" + curTransfer.getBarCode(),
                    "物美码：" + curTransfer.getSkuCode(),
                    "货主：" + curTransfer.getOwner(),
                    "规格：" + curTransfer.getPackName(),
                    "数量：" + curTransfer.getUomQty(),
                    curTransfer.getLocationCode());
        }
    }

    private void fillOutBound() {
        if (stockTransferView != null) {
            stockTransferView.showLocationConfirmView(
                    false,
                    "开始移库转出",
                    TextUtils.isEmpty(curTransfer.getTaskId()) ? null : "任务：" + curTransfer.getTaskId(),
                    "名称：" + curTransfer.getItemName(),
                    "国条码：" + curTransfer.getBarCode(),
                    "物美码：" + curTransfer.getSkuCode(),
                    "货主：" + curTransfer.getOwner(),
                    "规格：" + curTransfer.getPackName(),
                    "数量：" + curTransfer.getUomQty(),
                    curTransfer.getLocationCode());
        }
    }

    public boolean onBackPressed() {
        if (curTransfer == null) {
            activity.finish();
            return true;
        } else {
            if ("0".equals(curTransfer.getTaskId())) {
                curTransfer = null;
                stockTransferView.showScanLayout();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSubmitClick(String qty) {
        if (curTransfer != null) {
            if (TextUtils.isEmpty(qty)) {
                Toast.makeText(activity, "请输入正确的数量", Toast.LENGTH_SHORT).show();
                return;
            }

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

    public void onScanComplete(String locationCode, String barcode, String owner) {
        if (!TextUtils.isEmpty(locationCode)) {
            new ViewLocationTask(activity, uId, uToken, locationCode, barcode, owner).start();
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

                        stockTransferView.showItemView(
                                "填写转出数量",
                                TextUtils.isEmpty(curTransfer.getItemName()) ? null : "名称：" + curTransfer.getItemName(),
                                TextUtils.isEmpty(curTransfer.getBarCode()) ? null : "国条码：" + curTransfer.getBarCode(),
                                "物美码：" + curTransfer.getSkuCode(),
                                TextUtils.isEmpty(curTransfer.getOwner()) ? null : "货主：" + curTransfer.getOwner(),
                                TextUtils.isEmpty(curTransfer.getPackName()) ? null : "规格：" + curTransfer.getPackName(),
                                TextUtils.isEmpty(curTransfer.getUomQty()) ? null : "数量：" + curTransfer.getUomQty(),
                                "库位：" + curTransfer.getLocationCode(),
                                numQty);
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

        private String barcode;

        private String owner;

        public ViewLocationTask(Context context, String uId, String uToken, String locationCode, String barcode, String owner) {
            super(context, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.locationCode = locationCode;
            this.barcode = barcode;
            this.owner = owner;
        }

        @Override
        public DataHull<LocationView> doInBackground() {
            return ViewLocationProvider.request(context, uId, uToken, locationCode, barcode, owner);
        }

        @Override
        public void onPostExecute(LocationView result) {
            final boolean needBarcode = "1".equals(result.getNeedBarcode());
            final boolean needOwner = "1".equals(result.getNeedOwner());

            if (needBarcode || needOwner) {
                String msg = "请输入";
                if (needBarcode) {
                    msg += "商品国条码";
                }

                if (needOwner) {
                    msg += "和货主代码";
                }

                DialogTools.showOneButtonDialog(activity, msg, "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (needOwner) {
                            if (stockTransferView != null) {
                                stockTransferView.requestFocusOwner();
                            }
                        }

                        if (needBarcode) {
                            if (stockTransferView != null) {
                                stockTransferView.requestFocusBarcode();
                            }
                        }
                    }
                }, false);

                return;
            }

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
            transfer.setSkuCode(result.getSkuCode());
            transfer.setUom(result.getUom());
            transfer.setOwner(result.getOwner());

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
