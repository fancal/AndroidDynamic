package com.elianshang.wms.rf.plugin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.elianshang.wms.rf.bean.PluginSource;

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

    private PluginSource pluginSource;

    PluginDownloader(Context context, PluginSource pluginSource) {
        this.context = context;
        this.pluginSource = pluginSource;


    }

    @Override
    protected Boolean doInBackground(Void... params) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient mOkHttpClient = builder.build();

        Request request = new Request.Builder().url(pluginSource.getUrl()).build();
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
                    File apkFile = PluginTool.getDownloadApk(context, pluginSource.getFileName());
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
            PluginStarter pluginStarter = new PluginStarter(context, pluginSource);
            pluginStarter.execute();
        }
    }
}
