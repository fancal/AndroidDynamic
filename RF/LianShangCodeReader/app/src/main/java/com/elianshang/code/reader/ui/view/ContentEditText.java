package com.elianshang.code.reader.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * 内容判断EditText.
 */
public class ContentEditText extends EditText {

    public ContentEditText(Context context) {
        super(context);
    }

    public ContentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ContentEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    protected boolean isEmpty(String str){

        return TextUtils.isEmpty(str);
    }

    protected boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public boolean isRight(){
        String editStr = getText().toString().trim();
        return !isEmpty(editStr) && isNumeric(editStr);
    }
}
