package com.elianshang.wms.rf.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.dynamic.internal.DLPluginManager;
import com.elianshang.dynamic.internal.DLPluginPackage;
import com.elianshang.dynamic.utils.DLUtils;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.rf.BaseApplication;

import java.io.File;
import java.util.HashMap;

public class PluginTool {

    private static HashMap<String, DLPluginPackage> loadedList = new HashMap();

    public static void noteLoaded(String key, DLPluginPackage dlPluginPackage) {
        loadedList.put(key, dlPluginPackage);
    }

    public static boolean hasLoaded(String key) {
        return loadedList.containsKey(key);
    }

    public static DLPluginPackage getLoaded(String key) {
        return loadedList.get(key);
    }

    public static File getDownloadApk(Context context, String fileName) {
        File dir = context.getDir("plugin_apk", Context.MODE_PRIVATE);
        return new File(dir, fileName);
    }

    public static void load(Context context, String path) {
        File file = new File(path);
        Log.e("xue", path + " == " + MD5Tool.getMd5ByFile(file));

        PackageInfo packageInfo = DLUtils.getPackageInfo(context, path);
        DLPluginPackage dlPluginPackage = DLPluginManager.getInstance(context).loadApk(path);

        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
        DLIntent intent = new DLIntent(dlPluginPackage.packageName, packageInfo.activities[0].name);
        intent.putExtra("uId", BaseApplication.get().getUserId());
        intent.putExtra("uToken", BaseApplication.get().getUserToken());
        pluginManager.startPluginActivity(context, intent);
    }
}
