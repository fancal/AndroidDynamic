package com.elianshang.bridge.tool;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by liuhanzhi on 16/10/17.
 */

public class FloatUtils {

    public static boolean equals(String value1, String value2) {
        return TextUtils.equals(getFormatValue(value1),getFormatValue(value2));
    }

    public static boolean equals(String value1, Float value2) {
        return TextUtils.equals(getFormatValue(value1),getFormatValue(String.valueOf(value2)));
    }

    public static boolean equals(Float value1, Float value2) {
        return TextUtils.equals(getFormatValue(String.valueOf(value1)),getFormatValue(String.valueOf(value2)));
    }

    /**
     * 非法字段 默认为"0"
     * @param value
     * @return
     */
    private static String getFormatValue(String value){
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
            aft = aft.replaceAll("[.]$", "");//去掉多余的.
            aft = aft.replaceAll("^[.]", "0.");//开口的点加0

        } else {
            aft = "0";
        }

        if (TextUtils.isEmpty(aft)) {
            aft = "0";
        }

        return aft;
    }

    private static boolean ruleInteger(String text) {
        String ruleInteger = "^[0-9]\\d*$";
        if (TextUtils.isEmpty(ruleInteger)) {
            return true;
        }

        Pattern pattern = Pattern.compile(ruleInteger);
        return pattern.matcher(text).matches();
    }

    private static boolean ruleFloat(String text) {
        String ruleFloat = "^\\d*[.]\\d*$";
        if (TextUtils.isEmpty(ruleFloat)) {
            return true;
        }

        Pattern pattern = Pattern.compile(ruleFloat);
        return pattern.matcher(text).matches();
    }

}
