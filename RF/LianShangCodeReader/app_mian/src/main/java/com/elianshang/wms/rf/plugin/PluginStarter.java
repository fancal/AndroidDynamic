package com.elianshang.wms.rf.plugin;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.dynamic.internal.DLPluginManager;
import com.elianshang.dynamic.internal.DLPluginPackage;
import com.elianshang.tools.MD5Tool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.rf.BaseApplication;
import com.elianshang.wms.rf.bean.Menu;

import java.io.File;

/**
 * 插件启动器
 */
public class PluginStarter extends AsyncTask<Void, Void, DLPluginPackage> {

    private Context context;

    private Menu menu;

    private Dialog dialog;

    private boolean retry = false;

    private boolean needRestart = false;

    public PluginStarter(Context context, Menu menu) {
        this.context = context;
        this.menu = menu;
        this.retry = true;

        dialog = DialogTools.showLoadingDialog(context);
    }

    public PluginStarter(Context context, Menu menu, boolean retry) {
        this.context = context;
        this.menu = menu;
        this.retry = retry;

        dialog = DialogTools.showLoadingDialog(context);
    }

    @Override
    protected DLPluginPackage doInBackground(Void... params) {
        File apkFile = PluginTool.getDownloadApk(context, menu.getFileName());

        if (apkFile.exists()) {
            if (PluginTool.hasLoaded(menu.getFileName())) {
                DLPluginPackage dlPluginPackage = PluginTool.getLoaded(menu.getFileName());
                if (TextUtils.equals(dlPluginPackage.getIdentity(), menu.getIdentity())) {
                    return PluginTool.getLoaded(menu.getFileName());
                } else {
                    apkFile.delete();
                    needRestart = true;
                }
            } else {
                String fileMd5 = MD5Tool.getMd5ByFile(apkFile);
                if (TextUtils.equals(fileMd5, menu.getIdentity())) {
                    DLPluginPackage dlPluginPackage = DLPluginManager.getInstance(context).loadApk(apkFile.getAbsolutePath());
                    dlPluginPackage.setIdentity(menu.getIdentity());
                    PluginTool.noteLoaded(menu.getFileName(), dlPluginPackage);

                    return dlPluginPackage;
                } else {
                    apkFile.delete();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(DLPluginPackage dlPluginPackage) {
        if (dlPluginPackage != null) {
            DLPluginManager pluginManager = DLPluginManager.getInstance(context);
            DLIntent intent = new DLIntent(dlPluginPackage.packageName, dlPluginPackage.packageInfo.activities[0].name);
            intent.putExtra("uId", BaseApplication.get().getUserId());
            intent.putExtra("uToken", BaseApplication.get().getUserToken());
            pluginManager.startPluginActivity(context, intent);
        } else {
            if (retry) {
                PluginDownloader pluginDownloader = new PluginDownloader(context, menu, needRestart);
                pluginDownloader.execute();
            } else {
                ToastTool.show(context, "插件安装失败");
            }
        }

        if (dialog != null) {
            dialog.cancel();
        }
    }
}