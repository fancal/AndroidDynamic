package com.elianshang.wms.app.procurement.controller;

import android.app.Activity;

import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.view.ProcurementView;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseProcurementController {

    protected Procurement curProcurement;

    protected String uId;

    protected String uToken;

    protected Activity activity;

    protected ProcurementView procurementView;

    protected abstract void onSubmitClick(String qty , String scatterQty);

    protected abstract void onBindTaskClick();

    protected abstract void onComplete(String s);

    public BaseProcurementController(Activity activity,String uId, String uToken, Procurement curProcurement, ProcurementView procurementView) {
        this.activity = activity;
        this.uId = uId;
        this.uToken = uToken;
        this.curProcurement = curProcurement;
        this.procurementView = procurementView;
    }

    public interface TransferCompleteListener {
        void onTransferSuccess();
    }

}
