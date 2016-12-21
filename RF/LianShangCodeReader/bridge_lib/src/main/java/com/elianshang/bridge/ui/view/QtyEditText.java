package com.elianshang.bridge.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.regex.Pattern;

/**
 * Created by xfilshy on 16/8/11.
 */
public class QtyEditText extends ContentEditText {

    String rule = "^\\d*[.]?\\d*$";

    public QtyEditText(Context context) {
        super(context);
    }

    public QtyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QtyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QtyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean rule(String text) {
        if (TextUtils.isEmpty(rule)) {
            return true;
        }

        Pattern pattern = Pattern.compile(rule);
        return pattern.matcher(text).matches();
    }

    public String getValue() {
        String value = null;
        Editable editable = getText();
        if (editable != null) {
            value = editable.toString();
        }

        if (TextUtils.isEmpty(value)) {
            CharSequence charSequence = getHint();
            if (charSequence != null) {
                value = charSequence.toString();
            }
        }

        if (TextUtils.isEmpty(value)) {
            return "";
        }

        String aft;
        if (rule(value)) {
            aft = value.replaceAll("^0+(\\d+)", "$1");
            aft = aft.replaceAll("(\\.\\d+)0$", "$1");
            aft = aft.replaceAll("^\\.", "0.");
            aft = aft.replaceAll("\\.$", ".0");
            aft = aft.replaceAll("\\.0$", "");
        } else {
            aft = "0";
        }

        return aft;
    }
}
