package com.elianshang.wms.rf.plugin;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.rf.bean.PluginSource;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;

import java.io.File;

/**
 * 插件启动器
 */
public class PluginStarter extends AsyncTask<Void, Void, DLPluginPackage> {

    private Context context;

    private PluginSource pluginSource;

    public PluginStarter(Context context, PluginSource pluginSource) {
        this.context = context;
        this.pluginSource = pluginSource;
    }

    @Override
    protected DLPluginPackage doInBackground(Void... params) {
        File apkFile = PluginTool.getDownloadApk(context, pluginSource.getFileName());
        if (apkFile.exists()) {
            String fileMd5 = MD5Tool.getMd5ByFile(apkFile);
            if (TextUtils.equals(fileMd5, pluginSource.getIdentity())) {
                DLPluginPackage dlPluginPackage = DLPluginManager.getInstance(context).loadApk(apkFile.getAbsolutePath());

                return dlPluginPackage;
            } else {
                apkFile.delete();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(DLPluginPackage dlPluginPackage) {
        if (dlPluginPackage != null) {
            DLPluginManager pluginManager = DLPluginManager.getInstance(context);
            pluginManager.startPluginActivity(context, new DLIntent(dlPluginPackage.packageName, dlPluginPackage.packageInfo.activities[0].name));
        } else {
            PluginDownloader pluginDownloader = new PluginDownloader(context, pluginSource);
            pluginDownloader.execute();
        }
    }
}