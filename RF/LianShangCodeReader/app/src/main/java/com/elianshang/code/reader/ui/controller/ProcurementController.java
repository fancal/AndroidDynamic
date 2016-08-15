package com.elianshang.code.reader.ui.controller;

import android.app.Activity;

import com.elianshang.code.reader.ui.view.ProcurementView;
import com.elianshang.tools.ToastTool;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public class ProcurementController extends BaseProcurementController implements BaseProcurementController.TransferCompleteListener {

    private BaseProcurementController baseProcurementController;

    private boolean isFrom = true;

    public ProcurementController(Activity activity, String taskId, ProcurementView procurementView) {
        super(activity, taskId, procurementView);
        baseProcurementController = new ProcurementFromController(activity, taskId, procurementView);

        baseProcurementController.setTransferCompleteListener(this);
    }

    @Override
    public void onSubmitClick(String qty) {
        baseProcurementController.onSubmitClick(qty);
    }

    @Override
    public void onComplete(String s) {
        baseProcurementController.onComplete(s);
    }


    @Override
    public void onTransferSuccess() {
        if (isFrom) {
            isFrom = false;
            ToastTool.show(activity, "转出成功");
            detail = baseProcurementController.detail;
            baseProcurementController = new ProcurementToController(activity, taskId, procurementView, detail);
            baseProcurementController.setTransferCompleteListener(this);
        } else {
            ToastTool.show(activity, "转入成功");
            activity.finish();
        }
    }
}
