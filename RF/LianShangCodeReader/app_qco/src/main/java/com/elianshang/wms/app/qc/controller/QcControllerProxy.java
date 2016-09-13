package com.elianshang.wms.app.qc.controller;

import android.app.Activity;

public class QcControllerProxy extends BaseQcController {

    BaseQcController baseQcController;

    public QcControllerProxy(Activity activity, String uId, String uToken) {
        super(activity, uId, uToken);
    }

    public void releaseScanEditTextTool() {
        super.releaseCreateLayout();
    }

    @Override
    protected void releaseCreateLayout() {
        super.releaseCreateLayout();
        int type = qcList.getQcType();
        if (type == 1) {
            baseQcController = new QcScanController(activity, uId, uToken);
        } else if (type == 2) {
            baseQcController = new QcManualController(activity, uId, uToken);
        }
        baseQcController.qcList = qcList;
        baseQcController.containerId = containerId;
        baseQcController.releaseCreateLayout();
        baseQcController.fillQcListData();
    }

    @Override
    protected void fillQcListData() {
        baseQcController.fillQcListData();
    }

    @Override
    protected void onSubmitButtonClick() {
        baseQcController.onSubmitButtonClick();
    }

    @Override
    protected void onScan(String s) {
        baseQcController.onScan(s);
    }
}
