package com.elianshang.code.reader.tool;

import android.content.Context;

public class SecretTool {

    static {
        System.loadLibrary("lsh-private");
    }

    public static native String getBaseUrl(Context context);

    public static native String getHttpSecretKey(Context context);

    public static native String getLocalSecretKey(Context context);

    public static native String getOpensslSecretKey(Context context);

    public static native String getAliPayPartner(Context context);

    public static native String getAliPaySeller(Context context);

    public static native String getAliPayRsaPrivate(Context context);

    public static native String getAliPayNotifyUrl(Context context);

}
