package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.tools.ToastTool;

/**
 * Created by liuhanzhi on 16/8/12.
 */
public class TransferController extends BaseTransferController implements BaseTransferController.TransferCompleteListener {

    BaseTransferController baseTransferController;

    private boolean isFrom = true;

    public TransferController(Activity activity) {
        super(activity);
        baseTransferController = new TransferFromController(activity);
        baseTransferController.setTransferCompleteListener(this);
    }

    @Override
    public void setData(TaskTransferDetail detail, String taskId) {
        this.detail = detail;
        this.taskId = taskId;
        baseTransferController.setData(detail, taskId);
    }

    @Override
    protected void onSubmitClick() {
        baseTransferController.onSubmitClick();
    }

    @Override
    protected String getLocationId() {
        return baseTransferController.getLocationId();
    }

    @Override
    public void fillLocationLayout() {
        baseTransferController.fillLocationLayout();
    }

    @Override
    protected void onLocationConfirmSuccess() {
        baseTransferController.onLocationConfirmSuccess();
    }

    @Override
    protected void onLocationConfirmFailed() {
        baseTransferController.onLocationConfirmFailed();
    }

    @Override
    public void onTransferSuccess() {
        if (isFrom) {
            isFrom = false;
            baseTransferController = new TransferToController(activity);
            baseTransferController.setTransferCompleteListener(this);
            baseTransferController.setData(detail, taskId);
            baseTransferController.fillLocationLayout();
            ToastTool.show(activity, "转出成功");
        } else {
            ToastTool.show(activity, "转入成功");
            activity.finish();
        }
    }
}
