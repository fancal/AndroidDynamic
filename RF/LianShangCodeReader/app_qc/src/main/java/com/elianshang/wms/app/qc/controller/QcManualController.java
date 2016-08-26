package com.elianshang.wms.app.qc.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.tool.QcDialog;
import com.elianshang.wms.app.qc.view.QcManualView;

public class QcManualController extends BaseQcController implements QcManualView.QcManualControllerListener, DialogInterface.OnDismissListener {

    private QcManualView mQcManualView;
    /**
     * 记录弹窗dialog中的商品barcode
     */
    private String dialogBarCode;

    public QcManualController(Activity activity, String uId, String uToken) {
        super(activity, uId, uToken);
        findView();
    }

    private void findView() {
        mQcManualView = new QcManualView(activity);
        mQcManualView.setListener(this);

        mainView.removeAllViews();
        mainView.addView(mQcManualView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    private void noteItem(String curBarCode, String inputQty, String exceptionQty) {
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

        mQcManualView.setVisibility(View.VISIBLE);

        mQcManualView.fill(qcList, submitMap);

    }

    private String getProgress() {
        int progress = 0;
        int exception = 0;
        int total = 0;

        for (QcList.Item item : qcList) {
            if (!item.getItemName().startsWith("错货")) {
                total++;
                if (submitMap.containsKey(item.getBarCode())) {
                    progress++;
                }
            } else {
                if (submitMap.containsKey(item.getBarCode())) {
                    exception++;
                }
            }
        }

        if (exception > 0) {
            return progress + "/" + total + "  错货:" + exception;
        } else {
            return progress + "/" + total;
        }
    }

    @Override
    protected void onSubmitButtonClick() {
        checkSubmit();
    }

    @Override
    protected void onScan(final String s) {
        for (int i = 0; i < qcList.size(); i++) {
            if (TextUtils.equals(qcList.get(i).getBarCode(), s)) {
                mQcManualView.scrollToPositon(i);
                return;
            }
        }
        if (TextUtils.equals(s, dialogBarCode)) {
            return;
        }
        dialogBarCode = s;
        AlertDialog alertDialog = QcDialog.showQcExceptionDialog(activity, "错货(" + s + ")", "1", "0", false, "取消", "确认", null, new QcDialog.OnQcPositiveButtonClick() {
            @Override
            public void onClick(String inputQty, String shoddyQty) {
                if (TextUtils.equals(inputQty, "0") && TextUtils.equals(shoddyQty, "0")) {
                    return;
                }
                QcList.Item item = new QcList.Item();
                item.setBarCode(s);
                item.setItemName("错货(" + s + ")");
                item.setPackName("EA");
                item.setQty("无");
                qcList.add(item);
                noteItem(s, inputQty, shoddyQty);

                mQcManualView.notifySetDataChanged(getProgress());
                mQcManualView.scrollToPositon(qcList.size() - 1);
            }
        });
        alertDialog.setOnDismissListener(this);
    }


    @Override
    public void onItemSelect(QcList.Item item, int position, boolean isSelect) {
        if (isSelect) {
            noteItem(item.getBarCode(), String.valueOf(item.getQty()), "0");
        } else {
            if (removeMistakeItem(item)) {
                return;
            }
            noteItem(item.getBarCode(), null, null);
        }

        mQcManualView.notifyItemChanged(position, getProgress());
    }

    @Override
    public void onExceptionClick(final QcList.Item item, final int position) {
        if (TextUtils.equals(item.getBarCode(), dialogBarCode)) {
            return;
        }
        dialogBarCode = item.getBarCode();

        String qty;
        String exceptionQty;
        boolean hasKey = false;
        if (submitMap.containsKey(item.getBarCode())) {
            CacheQty cacheQty = submitMap.get(item.getBarCode());
            qty = cacheQty.qty;
            exceptionQty = cacheQty.exceptionQty;
            hasKey = true;
        } else {
            qty = item.getQty();
            exceptionQty = "0";
            hasKey = false;
        }
        AlertDialog alertDialog = QcDialog.showQcExceptionDialog(activity, item.getItemName(), qty, exceptionQty, hasKey, "取消", "确认", null, new QcDialog.OnQcPositiveButtonClick() {
            @Override
            public void onClick(String inputQty, String shoddyQty) {
                if (TextUtils.equals(inputQty, "0") && TextUtils.equals(shoddyQty, "0")) {
                    if (removeMistakeItem(item)) {
                        return;
                    }
                }
                noteItem(item.getBarCode(), inputQty, shoddyQty);

                mQcManualView.notifyItemChanged(position, getProgress());
            }
        });
        alertDialog.setOnDismissListener(this);
    }

    /**
     * 移除错货商品
     *
     * @param item
     * @return
     */
    private boolean removeMistakeItem(QcList.Item item) {
        if (item.getItemName().startsWith("错货")) {//错货商品取消勾选后 从列表中移除改商品
            qcList.remove(item);
            submitMap.remove(item.getBarCode());

            mQcManualView.notifySetDataChanged(getProgress());
            return true;
        }
        return false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dialogBarCode = null;
    }
}
