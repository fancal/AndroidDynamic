package com.elianshang.bridge.tool;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by liuhanzhi on 16/10/17.
 */

public class FloatUtils {

    public static boolean equals(String value1, String value2) {
        return TextUtils.equals(getFormatValue(value1), getFormatValue(value2));
    }

    public static boolean equals(String value1, Float value2) {
        return TextUtils.equals(getFormatValue(value1), getFormatValue(String.valueOf(value2)));
    }

    public static boolean equals(Float value1, Float value2) {
        return TextUtils.equals(getFormatValue(String.valueOf(value1)), getFormatValue(String.valueOf(value2)));
    }

    /**
     * 非法字段 默认为"0"
     *
     * @param value
     * @return
     */
    private static String getFormatValue(String value) {
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

        if (TextUtils.isEmpty(aft)) {
            aft = "0";
        }

        return aft;
    }

    private static boolean rule(String text) {
        String ruleInteger = "^\\d*[.]?\\d*$";
        if (TextUtils.isEmpty(ruleInteger)) {
            return true;
        }

        Pattern pattern = Pattern.compile(ruleInteger);
        return pattern.matcher(text).matches();
    }

}
