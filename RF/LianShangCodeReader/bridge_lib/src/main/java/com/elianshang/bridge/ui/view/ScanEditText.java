package com.elianshang.bridge.ui.view;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.elianshang.bridge.tool.DialogTools;


/**
 * 扫描EditText
 */
public class ScanEditText extends ContentEditText {

    private OnSetInputEnd inputEnd;

    public ScanEditText(Context context) {
        super(context);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setInputType(InputType.TYPE_NULL);
        Log.e("xue", "getContext() == " + getContext());
        setOnLongClickListener(getContext());
    }

    private void setOnLongClickListener(final Context context) {
        if (context == null) {
            return;
        }

        if (!(context instanceof Activity)) {
            return;
        }

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogTools.showEditViewDialog((Activity) context, "请入码值", "", "取消", "确认", null, new DialogTools.OnEditViewPositiveButtonClick() {
                    @Override
                    public void onClick(String editText) {
                        if (inputEnd != null && editText.trim().length() > 0) {
                            requestFocus();
                            inputEnd.onSetInputEnd(editText);
                        }
                    }
                }, false);

                return false;
            }
        });
    }

    public void setInputEnd(OnSetInputEnd inputEnd) {
        this.inputEnd = inputEnd;
    }

    /**
     * 输入完成的接口
     */
    public interface OnSetInputEnd {
        void onSetInputEnd(String s);
    }

}
