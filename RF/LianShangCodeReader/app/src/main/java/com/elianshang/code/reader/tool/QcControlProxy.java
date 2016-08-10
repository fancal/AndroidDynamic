package com.elianshang.code.reader.tool;

import android.app.Activity;

/**
 * Created by liuhanzhi on 16/8/10.
 */
public class QcControlProxy extends BaseQcController {

    BaseQcController baseQcController;

    public QcControlProxy(Activity activity) {
        super(activity);
    }


    @Override
    protected void releaseCreateLayout() {
        super.releaseCreateLayout();
        int type = 2;
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
