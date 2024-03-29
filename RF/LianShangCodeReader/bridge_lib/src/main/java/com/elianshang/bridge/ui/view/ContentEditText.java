package com.elianshang.bridge.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * 内容判断EditText.
 */
public class ContentEditText extends EditText {

    private String patternCompile = "";

//    private String patternCompile = "[0-9]*";

    public ContentEditText(Context context) {
        super(context);
    }

    public ContentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContentEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    protected boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    protected boolean isRule(String str) {
        if (TextUtils.isEmpty(patternCompile)) {
            return true;
        }

        Pattern pattern = Pattern.compile(patternCompile);
        return pattern.matcher(str).matches();
    }

    public boolean isRight() {
        String editStr = getText().toString().trim();
        return !isEmpty(editStr) && isRule(editStr);
    }
}
