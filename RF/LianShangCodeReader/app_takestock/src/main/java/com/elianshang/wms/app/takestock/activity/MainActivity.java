package com.elianshang.wms.app.takestock.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.takestock.bean.Restore;
import com.elianshang.wms.app.takestock.provider.RestoreProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), MainActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new RestoreTask(uId, uToken).start();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    /**
     * 领取盘点任务
     */
    private class RestoreTask extends HttpAsyncTask<Restore> {

        private String uId;

        private String uToken;

        public RestoreTask(String uid, String uToken) {
            super(MainActivity.this.that, true, true, false, false);
            this.uId = uid;
            this.uToken = uToken;
        }

        @Override
        public DataHull<Restore> doInBackground() {
            return RestoreProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(Restore result) {
            if (result.isDone()) {
                ScanActivity.launch(MainActivity.this, uId, uToken, "JH");
                MainActivity.this.finish();
            } else {
                TakeStockActivity.launch(MainActivity.this, uId, uToken, result.getTakeStockList(), null);
                MainActivity.this.finish();
            }
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            finish();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            finish();
        }

        @Override
        public void netNull() {
            super.netNull();
            finish();
        }
    }

}
