package com.elianshang.code.reader.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.widget.CircleProgressBar;
import com.elianshang.tools.UITool;


public class DialogTools {

    /**
     * 加载loading
     *
     * @param context
     * @return
     */
    public static AppCompatDialog showLoadingDialog(Context context) {
        if (context == null) {
            return null;
        }
        View view = new CircleProgressBar(context);
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.transparentDialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                UITool.dipToPx(context, 40), UITool.dipToPx(context, 40)));
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


        return dialog;
    }

    /**
     * 显示一个按钮的dialog
     */
    public static AlertDialog showOneButtonDialog(Activity context, int msg, int buttonText, final DialogInterface.OnClickListener okClickListener,
                                                  boolean cancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(buttonText, okClickListener);
        builder.setCancelable(cancelable);
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        return alertDialog;
    }

    /**
     * 显示一个按钮的dialog
     */
    public static AlertDialog showOneButtonDialog(Activity context, String msg, String buttonText, final DialogInterface.OnClickListener okClickListener,
                                                  boolean cancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(buttonText, okClickListener);
        builder.setCancelable(cancelable);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }

    /**
     * 显示二个按钮的dialog
     */
    public static AlertDialog showTwoButtonDialog(Activity context, int msg, int button1Text, int button2Text,
                                                  final DialogInterface.OnClickListener clickListener1, final DialogInterface.OnClickListener clickListener2, boolean cancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }
        return showTwoButtonDialog(context, context.getString(msg), context.getString(button1Text), context.getString(button2Text), clickListener1, clickListener2, cancelable);

    }

    /**
     * 显示二个按钮的dialog
     */
    public static AlertDialog showTwoButtonDialog(Activity context, String msg, String button1Text, String button2Text, final DialogInterface.OnClickListener clickListener1,
                                                  final DialogInterface.OnClickListener clickListener2, boolean cancelable) {

        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton(button1Text, clickListener1).setPositiveButton(button2Text, clickListener2);
        builder.setCancelable(cancelable);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;

    }

    /**
     * 显示二个按钮的dialog（带title）
     */
    public static AlertDialog showTwoButtonDialog(Activity context, String tile, String msg, String button1Text, String button2Text, final DialogInterface.OnClickListener clickListener1,
                                                  final DialogInterface.OnClickListener clickListener2, boolean cancelable) {

        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tile);
        builder.setMessage(msg);
        builder.setNegativeButton(button1Text, clickListener1).setPositiveButton(button2Text, clickListener2);
        builder.setCancelable(cancelable);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;

    }

    /**
     * 显示三个按钮的dialog
     */
    public static AlertDialog showThereButtonDialog(Activity context, int msg, int button1Text, int button2Text, int button3Text,
                                                    final DialogInterface.OnClickListener clickListener1, final DialogInterface.OnClickListener clickListener2, final DialogInterface.OnClickListener clickListener3, boolean cancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }
        return showThereButtonDialog(context, context.getString(msg), context.getString(button1Text), context.getString(button2Text), context.getString(button3Text), clickListener1, clickListener2, clickListener3, cancelable);
    }

    /**
     * 显示三个按钮的dialog
     */
    public static AlertDialog showThereButtonDialog(Activity context, String msg, String button1Text, String button2Text, String button3Text,
                                                    final DialogInterface.OnClickListener clickListener1, final DialogInterface.OnClickListener clickListener2, final DialogInterface.OnClickListener clickListener3, boolean cancelable) {
        if (context == null || context.isFinishing()) {
            return null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton(button1Text, clickListener1).setNeutralButton(button2Text, clickListener2).setPositiveButton(button3Text, clickListener3);
        builder.setCancelable(cancelable);

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        return alertDialog;
    }

    /**
     * 显示带输入框的dialog（带title）
     */
    public static AlertDialog showEditViewDialog(Activity context, String tile, String textHint, String button1Text, String button2Text, final DialogInterface.OnClickListener clickListener1,
                                                 final OnEditViewPositiveButtonClick clickListener2, boolean cancelable) {

        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        LayoutInflater inflater = context.getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_editview, null);
        final AppCompatEditText editText = (AppCompatEditText) layout.findViewById(R.id.edittext);
        AppCompatTextView titleView = (AppCompatTextView) layout.findViewById(R.id.title);
        titleView.setText(tile);
        if (!TextUtils.isEmpty(textHint)) {
            editText.setHint(textHint);
        }
        builder.setNegativeButton(button1Text, clickListener1).setPositiveButton(button2Text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != clickListener2) {
                    clickListener2.onClick(editText.getText().toString());
                }
            }
        });
        builder.setCancelable(cancelable);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(layout);
        //强制弹出键盘
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return alertDialog;

    }

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
        final EditText detailInputQtyEditText = (EditText) layout.findViewById(R.id.inputQty_EditView);
        final EditText detailShoddynQtyEditText = (EditText) layout.findViewById(R.id.shoddyQty_EditView);
        if (!TextUtils.isEmpty(title)) {
            TextView barcodeTextView = (TextView) layout.findViewById(R.id.title_barcode);
            barcodeTextView.setText("名称:" + title);
            barcodeTextView.setVisibility(View.VISIBLE);
        }
        if (isSetText) {
            detailInputQtyEditText.setHint(null);
            detailInputQtyEditText.setText(String.valueOf(qty));
            detailShoddynQtyEditText.setHint(null);
            detailShoddynQtyEditText.setText(String.valueOf(exceptionQty));
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
                    String inputQty = detailInputQtyEditText.getText().toString();
                    if (TextUtils.isEmpty(inputQty)) {
                        if (detailInputQtyEditText.getHint() != null) {
                            inputQty = detailInputQtyEditText.getHint().toString();
                        }
                    }

                    String shoddyQty = detailShoddynQtyEditText.getText().toString();
                    if (TextUtils.isEmpty(shoddyQty)) {
                        if (detailShoddynQtyEditText.getHint() != null) {
                            shoddyQty = detailShoddynQtyEditText.getHint().toString();
                        }
                    }
                    if (TextUtils.isEmpty(inputQty)) {
                        inputQty = "0";
                    }
                    if (TextUtils.isEmpty(shoddyQty)) {
                        inputQty = "0";
                    }
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

    public interface OnEditViewPositiveButtonClick {
        void onClick(String editText);
    }

    public interface OnQcPositiveButtonClick {
        void onClick(String inputQty, String shoddyQty);
    }

    public interface OnPayWayItemClick {
        void onClick(int position);
    }


}
