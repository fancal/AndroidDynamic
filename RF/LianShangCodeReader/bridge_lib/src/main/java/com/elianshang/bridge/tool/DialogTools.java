package com.elianshang.bridge.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.R;
import com.elianshang.bridge.ui.widget.CircleProgressBar;
import com.elianshang.tools.UITool;


public class DialogTools {

    /**
     * 加载loading
     *
     * @param context
     * @return
     */
    public static Dialog showLoadingDialog(Context context, String msg) {
        if (context == null) {
            return null;
        }

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        CircleProgressBar circleProgressBar = new CircleProgressBar(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UITool.dipToPx(context, 40), UITool.dipToPx(context, 40));
        circleProgressBar.setLayoutParams(layoutParams);

        final TextView textView = new TextView(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = UITool.dipToPx(context, 20);
        textView.setLayoutParams(layoutParams);
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setTextColor(0xFFFFFFFF);
        textView.setText(msg);

        linearLayout.addView(circleProgressBar);
        linearLayout.addView(textView);

        final Dialog dialog = new Dialog(context, R.style.B_TransparentDialog);
        dialog.setContentView(linearLayout);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

    /**
     * 加载loading
     *
     * @param context
     * @return
     */
    public static Dialog showLoadingDialog(Context context) {
        if (context == null) {
            return null;
        }
        View view = new CircleProgressBar(context);
        final Dialog dialog = new Dialog(context, R.style.B_TransparentDialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(UITool.dipToPx(context, 40), UITool.dipToPx(context, 40)));
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
                                                 final OnEditViewPositiveButtonClick clickListener2, boolean cancelable, boolean isCode) {

        if (context == null || context.isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new WindowManager.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = UITool.dipToPx(context, 16);
        layoutParams.topMargin = UITool.dipToPx(context, 16);
        layoutParams.rightMargin = UITool.dipToPx(context, 20);
        layoutParams.leftMargin = UITool.dipToPx(context, 20);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        final EditText editText = new EditText(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = UITool.dipToPx(context, 16);
        layoutParams.topMargin = UITool.dipToPx(context, 16);
        layoutParams.rightMargin = UITool.dipToPx(context, 20);
        layoutParams.leftMargin = UITool.dipToPx(context, 20);
        editText.setLayoutParams(layoutParams);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        if (isCode) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setSingleLine();

        linearLayout.addView(textView);
        linearLayout.addView(editText);

        textView.setText(tile);
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
        alertDialog.setView(linearLayout);
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
}
