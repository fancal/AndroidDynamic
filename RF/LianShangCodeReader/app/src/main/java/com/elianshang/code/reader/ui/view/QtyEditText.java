package com.elianshang.code.reader.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.regex.Pattern;

/**
 * Created by xfilshy on 16/8/11.
 */
public class QtyEditText extends ContentEditText {

    String ruleInteger = "^[0-9]\\d*$";

    String ruleFloat = "^\\d*.\\d*$";

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

    private boolean ruleInteger(String text) {
        if (TextUtils.isEmpty(ruleInteger)) {
            return true;
        }

        Pattern pattern = Pattern.compile(ruleInteger);
        return pattern.matcher(text).matches();
    }

    private boolean ruleFloat(String text) {
        if (TextUtils.isEmpty(ruleFloat)) {
            return true;
        }

        Pattern pattern = Pattern.compile(ruleFloat);
        return pattern.matcher(text).matches();
    }

    public String getValue() {
        String value = null;
        Editable editable = getText();
        if (editable == null) {
            CharSequence charSequence = getHint();
            if (charSequence != null) {
                value = charSequence.toString();
            }
        } else {
            value = editable.toString();
        }

        int type = 0;
        if (ruleInteger(value)) {
            type = 1;
        }

        if (ruleFloat(value)) {
            type = 2;
        }

        String aft;
        if (type == 1) {
            aft = value.replaceAll("0*(\\d+)", "$1");
        } else if (type == 2) {
            aft = value.replaceAll("0*(\\d+)", "$1");
            aft = aft.replaceAll("0+?$", "");//去掉多余的0
            aft = aft.replaceAll("[.]$", "");
            aft = aft.replaceAll("^[.]", "0.");

        } else {
            aft = "0";
        }

        return aft;
    }
}
