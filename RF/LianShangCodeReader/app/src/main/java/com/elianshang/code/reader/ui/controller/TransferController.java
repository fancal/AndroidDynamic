package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.tools.ToastTool;

/**
 * Created by liuhanzhi on 16/8/12.
 */
public class TransferController implements ScanManager.OnBarCodeListener, BaseTransferController.TransferCompleteListener {

    BaseTransferController baseTransferController;

    private boolean isFrom = true;

    private Activity activity;

    private TaskTransferDetail detail;

    private String taskId;

    public TransferController(Activity activity) {
        this.activity = activity;
        baseTransferController = new TransferFromController(activity);
        baseTransferController.setTransferCompleteListener(this);
    }

    public void setData(TaskTransferDetail detail, String taskId) {
        this.detail = detail;
        this.taskId = taskId;
        baseTransferController.setData(detail, taskId);
    }

    public void fillLocationLayout() {
        baseTransferController.fillLocationLayout();
    }

    @Override
    public void onTransferSuccess() {
        if (isFrom) {
            isFrom = false;
            if (baseTransferController != null) {
                baseTransferController.release();
                baseTransferController = null;
            }
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

    @Override
    public void OnBarCodeReceived(String s) {
        baseTransferController.OnBarCodeReceived(s);
    }

}
