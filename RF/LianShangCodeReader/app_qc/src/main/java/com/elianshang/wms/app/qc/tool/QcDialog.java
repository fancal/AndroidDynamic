package com.elianshang.wms.app.qc.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.wms.app.qc.R;

public class QcDialog {

    /**
     * QC 异常dialog
     */
    public static AlertDialog showQcExceptionDialog(Activity context, String title, String qty, String exceptionQty, boolean isSetText, String button1Text, String button2Text, final DialogInterface.OnClickListener clickListener1,
                                                    final OnQcPositiveButtonClick clickListener2) {

        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_qc_exception, null);
        final QtyEditText detailInputQtyEditText = (QtyEditText) layout.findViewById(R.id.inputQty_EditView);
        final QtyEditText detailShoddynQtyEditText = (QtyEditText) layout.findViewById(R.id.shoddyQty_EditView);
        if (!TextUtils.isEmpty(title)) {
            TextView barcodeTextView = (TextView) layout.findViewById(R.id.title_barcode);
            barcodeTextView.setText("名称:" + title);
            barcodeTextView.setVisibility(View.VISIBLE);
        }
        if (isSetText) {
            detailInputQtyEditText.setHint(String.valueOf(qty));
            detailInputQtyEditText.setText(null);
            detailShoddynQtyEditText.setHint(String.valueOf(exceptionQty));
            detailShoddynQtyEditText.setText(null);
        } else {
            detailInputQtyEditText.setText(null);
            detailInputQtyEditText.setHint(String.valueOf(qty));
            detailShoddynQtyEditText.setText(null);
            detailShoddynQtyEditText.setHint("0");
        }

        builder.setNegativeButton(button1Text, clickListener1).setPositiveButton(button2Text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != clickListener2) {
                    String inputQty = detailInputQtyEditText.getValue();
                    String shoddyQty = detailShoddynQtyEditText.getValue();
                    clickListener2.onClick(inputQty, shoddyQty);
                }
            }
        });
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(layout);
        //强制弹出键盘
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return alertDialog;

    }


    public interface OnQcPositiveButtonClick {
        void onClick(String inputQty, String shoddyQty);
    }

}
