package com.elianshang.wms.rf.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.elianshang.wms.rf.BaseApplication;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.utils.DLUtils;

import java.io.File;

public class PluginTool {

    public static File getDownloadApk(Context context, String fileName) {
        File dir = context.getDir("plugin_apk", Context.MODE_PRIVATE);
        return new File(dir, fileName);
    }

    public static void load(Context context, String path) {
        PackageInfo packageInfo = DLUtils.getPackageInfo(context, path);
        DLPluginPackage dlPluginPackage = DLPluginManager.getInstance(context).loadApk(path);

        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
        DLIntent intent = new DLIntent(dlPluginPackage.packageName, packageInfo.activities[0].name);
        intent.putExtra("uId", BaseApplication.get().getUserId());
        intent.putExtra("uToken", BaseApplication.get().getUserToken());
        pluginManager.startPluginActivity(context, intent);
    }
}
