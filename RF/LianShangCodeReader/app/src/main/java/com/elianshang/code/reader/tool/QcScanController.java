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
        if(mQcScanView.waitLayout.getVisibility() != View.VISIBLE){
            noteItem();
        }
        curBarCode = barcode;

        if (qcList != null && qcList.size() > 0) {
            for (QcList.Item item : qcList) {
                if (TextUtils.equals(barcode, item.getBarCode())) {
                    mQcScanView.fillDetailData(item);
                    return;
                }
            }
            mQcScanView.fillDetailDataNull(barcode);
        }
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
