package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;

import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.view.StockTransferView;


/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseStockTransferController {

    protected Transfer curTransfer;

    protected String uId;

    protected String uToken;

    protected Activity activity;

    protected StockTransferView stockTransferView;

    protected abstract void onSubmitClick(String qty);

    protected abstract void onWorkComplete(String s);

    public BaseStockTransferController(Activity activity, String uId, String uToken, Transfer curProcurement, StockTransferView procurementView) {
        this.activity = activity;
        this.uId = uId;
        this.uToken = uToken;
        this.curTransfer = curProcurement;
        this.stockTransferView = procurementView;
    }

    public interface TransferCompleteListener {
        void onTransferSuccess();
    }

}
