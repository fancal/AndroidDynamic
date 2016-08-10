package com.elianshang.code.reader.tool;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.bean.QcList;
import com.elianshang.code.reader.ui.view.QcScanView;

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
        mQcScanView = (QcScanView) activity.findViewById(R.id.scan_view);

    }

    private void findItem(final String barcode) {
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
        int progress;
        if (submitMap.containsKey(item.getBarCode())) {
            progress = submitMap.size();
        } else {
            progress = submitMap.size() + 1;
        }
        String detailProgress = progress + "/" + qcList.size();
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
        int progress = 0;
        if (submitMap.containsKey(barCode)) {
            progress = submitMap.size();
        } else {
            progress = submitMap.size() + 1;
        }

        String detailProgress = progress + "/" + qcList.size();
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

    private void noteItem() {
        String inputQty = mQcScanView.inputQtyText();
        String exceptionQty = mQcScanView.shoddyQtyText();

        if (TextUtils.isEmpty(inputQty)) {
            inputQty = mQcScanView.inputQtyHintText();
        }

        if (TextUtils.isEmpty(exceptionQty)) {
            exceptionQty = mQcScanView.shoddyQtyHintText();
        }

        if (!TextUtils.isEmpty(inputQty) || !TextUtils.isEmpty(exceptionQty)) {
            float fiqty = Float.parseFloat(inputQty);
            float feqty = Float.parseFloat(exceptionQty);
            CacheQty cacheQty = new CacheQty();
            cacheQty.qty = fiqty;
            if (feqty != 0) {
                cacheQty.exceptionQty = feqty;
                cacheQty.exceptionType = 1;
            }

            submitMap.put(curBarCode, cacheQty);
        } else {
            submitMap.remove(curBarCode);
        }
    }

    @Override
    protected void fillQcListData() {

        mQcScanView.setVisibility(View.VISIBLE);

        mQcScanView.fill(qcList, submitMap);
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
