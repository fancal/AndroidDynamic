package com.elianshang.wms.rf.plugin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.rf.bean.Menu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 插件下载器
 */
public class PluginDownloader extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    private Menu menu;

    private Dialog dialog;

    private boolean needRestart;

    PluginDownloader(Context context, Menu menu, boolean needRestart) {
        this.context = context;
        this.menu = menu;
        this.needRestart = needRestart;

        this.dialog = DialogTools.showLoadingDialog(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient mOkHttpClient = builder.build();

        Request request = new Request.Builder().url(menu.getUrl()).build();
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File apkFile = PluginTool.getDownloadApk(context, menu.getFileName());
                    fos = new FileOutputStream(apkFile);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                    return true;
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            if (needRestart) {
                if (context instanceof Activity) {
                    DialogTools.showOneButtonDialog((Activity) context, "加载新版插件,需要重启", "知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Process.killProcess(Process.myPid());
                        }
                    }, false);
                }
            } else {
                PluginStarter pluginStarter = new PluginStarter(context, menu, false);
                pluginStarter.execute();
            }
        } else {
            ToastTool.show(context, "插件下载失败");
        }

        if (dialog != null) {
            dialog.cancel();
        }
    }
}
