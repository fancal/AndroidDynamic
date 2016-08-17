package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.ui.view.ProcurementView;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseProcurementController {

    protected TaskTransferDetail detail;

    protected String taskId;

    protected Activity activity;

    protected ProcurementView procurementView;

    protected TransferCompleteListener mTransferCompleteListener;

    protected abstract void onSubmitClick(String qty);

    protected abstract void onComplete(String s);

    public BaseProcurementController(Activity activity, String taskId, ProcurementView procurementView) {
        this.activity = activity;
        this.taskId = taskId;
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
