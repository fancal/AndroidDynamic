package com.elianshang.code.reader.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.elianshang.code.reader.tool.DialogTools;

/**
 * 扫描EditText
 */
public class ScanEditText extends ContentEditText {

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
    }

    public void setOnLongClickListener(final Activity activity) {
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogTools.showTwoButtonDialog(activity, "确认手动编辑?", "取消", "确认", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setInputType(InputType.TYPE_CLASS_NUMBER);
                        requestFocus();
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    }
                }, false);

                return false;
            }
        });
    }

    public boolean isRight(){
        String editStr = getText().toString().trim();
        return !isEmpty(editStr) && isNumeric(editStr);
    }


}
