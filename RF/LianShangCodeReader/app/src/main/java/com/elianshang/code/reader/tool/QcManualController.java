package com.elianshang.code.reader.tool;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.bean.QcList;
import com.elianshang.code.reader.ui.view.QcManualView;

/**
 * Created by liuhanzhi on 16/8/10.
 */
public class QcManualController extends BaseQcController implements QcManualView.QcManualControllerListener {

    private QcManualView mQcManualView;

    public QcManualController(Activity activity) {
        super(activity);
        findView();
    }

    private void findView() {
        mQcManualView = (QcManualView) activity.findViewById(R.id.manual_view);

        mQcManualView.setListener(this);

    }


    private void noteItem(String curBarCode, String inputQty, String exceptionQty) {
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

        mQcManualView.setVisibility(View.VISIBLE);

        mQcManualView.fill(qcList, submitMap);

    }

    @Override
    protected void onSubmitButtonClick() {
        checkSubmit();
    }

    @Override
    protected void onScan(final String s) {
        DialogTools.showQcExceptionDialog(activity, "错货(" + s + ")", 1, 0, false, "取消", "确认", null, new DialogTools.OnQcPositiveButtonClick() {
            @Override
            public void onClick(String inputQty, String shoddyQty) {
                QcList.Item item = new QcList.Item();
                item.setBarCode(s);
                item.setItemName("错货(" + s + ")");
                item.setPackName("EA");
                qcList.add(item);
                noteItem(s, inputQty, shoddyQty);

                mQcManualView.notifySetDataChanged();
            }
        });
    }


    @Override
    public void onItemSelect(QcList.Item item, boolean isSelect) {
        if (isSelect) {
            noteItem(item.getBarCode(), String.valueOf(item.getQty()), "0");
        } else {
            noteItem(item.getBarCode(), null, null);
        }
    }

    @Override
    public void onExceptionClick(final QcList.Item item) {

        float qty;
        float exceptionQty;
        boolean hasKey = false;
        if (submitMap.containsKey(item.getBarCode())) {
            CacheQty cacheQty = submitMap.get(item.getBarCode());
            qty = cacheQty.qty;
            exceptionQty = cacheQty.exceptionQty;
            hasKey = true;
        } else {
            qty = item.getQty();
            exceptionQty = 0;
            hasKey = false;
        }
        DialogTools.showQcExceptionDialog(activity, null, qty, exceptionQty, hasKey, "取消", "确认", null, new DialogTools.OnQcPositiveButtonClick() {
            @Override
            public void onClick(String inputQty, String shoddyQty) {
                noteItem(item.getBarCode(), inputQty, shoddyQty);

                mQcManualView.notifySetDataChanged();
            }
        });
    }

}
