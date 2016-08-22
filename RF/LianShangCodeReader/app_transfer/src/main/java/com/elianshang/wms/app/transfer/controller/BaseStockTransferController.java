package com.elianshang.wms.app.transfer.controller;

import android.app.Activity;

import com.elianshang.wms.app.transfer.bean.StockTransfer;
import com.elianshang.wms.app.transfer.ui.view.StockTransferView;


/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseStockTransferController {

    protected StockTransfer curStockTransfer;

    protected String uId;

    protected String uToken;

    protected Activity activity;

    protected StockTransferView stockTransferView;

    protected abstract void onSubmitClick(String qty);

    protected abstract void onComplete(String s);

    public BaseStockTransferController(Activity activity, String uId, String uToken, StockTransfer curProcurement, StockTransferView procurementView) {
        this.activity = activity;
        this.uId = uId;
        this.uToken = uToken;
        this.curStockTransfer = curProcurement;
        this.stockTransferView = procurementView;
    }

    public interface TransferCompleteListener {
        void onTransferSuccess();
    }

}
