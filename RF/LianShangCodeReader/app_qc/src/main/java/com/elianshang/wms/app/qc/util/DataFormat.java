package com.elianshang.wms.app.qc.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by liuhanzhi on 16/10/13.
 */

public class DataFormat {

    public static String getFormatValue(String value){
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
