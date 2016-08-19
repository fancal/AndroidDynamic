package com.elianshang.wms.app.procurement.controller;

import android.app.Activity;

import com.elianshang.wms.app.procurement.bean.TaskTransferDetail;
import com.elianshang.wms.app.procurement.ui.view.ProcurementView;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseProcurementController {

    protected TaskTransferDetail detail;

    protected String taskId;

    protected String uId;

    protected Activity activity;

    protected ProcurementView procurementView;

    protected TransferCompleteListener mTransferCompleteListener;

    protected abstract void onSubmitClick(String qty);

    protected abstract void onComplete(String s);

    public BaseProcurementController(Activity activity, String taskId, String uId, ProcurementView procurementView) {
        this.activity = activity;
        this.taskId = taskId;
        this.uId = uId;
        this.procurementView = procurementView;
    }

    public TransferCompleteListener getTransferCompleteListener() {
        return mTransferCompleteListener;
    }

    public void setTransferCompleteListener(TransferCompleteListener mTransferCompleteListener) {
        this.mTransferCompleteListener = mTransferCompleteListener;
    }

    public interface TransferCompleteListener {
        void onTransferSuccess();
    }

}
