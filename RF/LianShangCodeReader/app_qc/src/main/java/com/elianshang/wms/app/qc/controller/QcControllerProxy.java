package com.elianshang.wms.app.qc.controller;

import android.app.Activity;

/**
 * Created by liuhanzhi on 16/8/10.
 */
public class QcControllerProxy extends BaseQcController {

    BaseQcController baseQcController;

    public QcControllerProxy(Activity activity) {
        super(activity);
    }


    @Override
    protected void releaseCreateLayout() {
        super.releaseCreateLayout();
        int type = qcList.getQcType();
        if (type == 1) {
            baseQcController = new QcScanController(activity);
        } else if (type == 2) {
            baseQcController = new QcManualController(activity);
        }
        baseQcController.qcList = qcList;
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
