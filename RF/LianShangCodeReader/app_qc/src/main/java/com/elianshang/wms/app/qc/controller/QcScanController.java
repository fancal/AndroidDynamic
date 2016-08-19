package com.elianshang.wms.app.qc.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.ui.view.QcScanView;


/**
 * Created by liuhanzhi on 16/8/10.
 */
public class QcScanController extends BaseQcController {

    private QcScanView mQcScanView;

    private String curBarCode;


    public QcScanController(Activity activity) {
        super(activity);
        findView();
    }

    private void findView() {
        mQcScanView = new QcScanView(activity);

        mainView.removeAllViews();
        mainView.addView(mQcScanView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void findItem(final String barcode) {
        if (submitButton.getVisibility() != View.VISIBLE) {
            submitButton.setVisibility(View.VISIBLE);
        }
        if (mQcScanView.waitLayout.getVisibility() != View.VISIBLE) {
            noteItem();
        }
        curBarCode = barcode;

        if (qcList != null && qcList.size() > 0) {
            for (QcList.Item item : qcList) {
                if (TextUtils.equals(barcode, item.getBarCode())) {
                    fillDetailData(item);
                    return;
                }
            }
            fillDetailDataNull(barcode);
        }
    }

    public void fillDetailData(QcList.Item item) {
        String detailProgress = getProgress(item.getBarCode());
        String itemName = item.getItemName();
        String itemPackName = item.getPackName();
        String itemQty = String.valueOf(item.getQty());
        String inputQty;
        String inputQtyHint;
        String shoddyQty;
        String shoddyQtyHint;

        if (submitMap.containsKey(item.getBarCode())) {
            BaseQcController.CacheQty cacheQty = submitMap.get(item.getBarCode());
            inputQty = String.valueOf(cacheQty.qty);
            inputQtyHint = null;
            shoddyQty = String.valueOf(cacheQty.exceptionQty);
            shoddyQtyHint = null;
        } else {
            inputQty = null;
            inputQtyHint = String.valueOf(item.getQty());
            shoddyQty = null;
            shoddyQtyHint = "0";
        }

        mQcScanView.fillDetailData(detailProgress, itemName, itemPackName, itemQty, inputQty, inputQtyHint, shoddyQty, shoddyQtyHint);
    }

    /**
     * 填充QC列表不存在的商品
     */
    public void fillDetailDataNull(String barCode) {

        String detailProgress = getProgress(barCode);
        String itemName = "错品(" + barCode + ")";
        String itemPackName = "请按EA查点数量";
        String itemQty = "如果是误扫,请输入0,或不要输入数量";
        String inputQty;
        String inputQtyHint;
        String shoddyQty;
        String shoddyQtyHint;

        if (submitMap.containsKey(barCode)) {
            BaseQcController.CacheQty cacheQty = submitMap.get(barCode);
            inputQty = String.valueOf(cacheQty.qty);
            inputQtyHint = null;
            shoddyQty = String.valueOf(cacheQty.exceptionQty);
            shoddyQtyHint = null;
        } else {
            inputQty = null;
            inputQtyHint = "1";
            shoddyQty = null;
            shoddyQtyHint = "0";
        }

        mQcScanView.fillDetailData(detailProgress, itemName, itemPackName, itemQty, inputQty, inputQtyHint, shoddyQty, shoddyQtyHint);
    }


    private String getProgress(String barCode) {
        int progress = 0;
        boolean flag = false;

        for (QcList.Item item : qcList) {
            if (submitMap.containsKey(item.getBarCode())) {
                progress++;
            }

            if (item.getBarCode().equals(barCode)) {
                flag = true;
            }
        }
        int exception = submitMap.size() - progress;
        if (!submitMap.containsKey(barCode)) {
            if (flag) {
                progress++;
            } else {
                exception++;
            }
        }

        if (exception > 0) {
            return progress + "/" + qcList.size() + "  错货:" + exception;
        } else {
            return progress + "/" + qcList.size();
        }
    }

    private void noteItem() {
        String inputQty = mQcScanView.getInputQtyValue();
        String exceptionQty = mQcScanView.getShoddyQtyValue();


        if (!TextUtils.isEmpty(inputQty) || !TextUtils.isEmpty(exceptionQty)) {
            CacheQty cacheQty = new CacheQty();
            cacheQty.qty = inputQty;
            if (!"0".equals(exceptionQty)) {
                cacheQty.exceptionQty = exceptionQty;
                cacheQty.exceptionType = "1";
            }

            submitMap.put(curBarCode, cacheQty);
        } else {
            submitMap.remove(curBarCode);
        }
    }

    @Override
    protected void fillQcListData() {

        mQcScanView.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

    }

    @Override
    protected void onSubmitButtonClick() {
        noteItem();
        checkSubmit();
    }

    @Override
    protected void onScan(String s) {
        findItem(s);
    }
}
