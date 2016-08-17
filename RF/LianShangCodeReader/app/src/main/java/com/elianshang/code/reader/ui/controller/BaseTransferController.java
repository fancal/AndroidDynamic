package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.ui.view.TransferView;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public abstract class BaseTransferController {

    protected TaskTransferDetail detail;

    protected String taskId;

    protected Activity activity;

    protected TransferView transferView;

    protected TransferCompleteListener mTransferCompleteListener;

    protected abstract void onSubmitClick(String qty);

    protected abstract void onComplete(String s);

    public BaseTransferController(Activity activity, String taskId, TransferView transferView) {
        this.activity = activity;
        this.taskId = taskId;
        this.transferView = transferView;
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
