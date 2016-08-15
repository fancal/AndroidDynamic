package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.ui.view.TransferView;
import com.elianshang.tools.ToastTool;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class TransferController extends BaseTransferController implements BaseTransferController.TransferCompleteListener {

    private BaseTransferController baseTransferController;

    private boolean isFrom = true;

    public TransferController(Activity activity, String taskId, TransferView transferView) {
        super(activity, taskId, transferView);
        baseTransferController = new TransferFromController(activity, taskId, transferView);

        baseTransferController.setTransferCompleteListener(this);
    }

    @Override
    public void onSubmitClick(String qty) {
        baseTransferController.onSubmitClick(qty);
    }

    @Override
    public void onComplete(String s) {
        baseTransferController.onComplete(s);
    }


    @Override
    public void onTransferSuccess() {
        if (isFrom) {
            isFrom = false;
            ToastTool.show(activity, "转出成功");
            detail = baseTransferController.detail;
            baseTransferController = new TransferToController(activity, taskId, transferView, detail);
            baseTransferController.setTransferCompleteListener(this);
        } else {
            ToastTool.show(activity, "转入成功");
            activity.finish();
        }
    }
}
