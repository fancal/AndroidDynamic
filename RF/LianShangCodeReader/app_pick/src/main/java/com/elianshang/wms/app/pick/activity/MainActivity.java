package com.elianshang.wms.app.pick.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.pick.bean.Restore;
import com.elianshang.wms.app.pick.provider.RestoreProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    private String uId;

    private String uToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new FetchProcurementTask(uId, uToken).start();
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

    private class FetchProcurementTask extends HttpAsyncTask<Restore> {

        private String uId;

        private String uToken;

        public FetchProcurementTask(String uId, String uToken) {
            super(MainActivity.this.that, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<Restore> doInBackground() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RestoreProvider.request(uId, uToken);
        }

        @Override
        public void onPostExecute(Restore result) {
            if (result.isDone()) {
                PickActivity.launch(MainActivity.this, uId, uToken, null);
                MainActivity.this.finish();
            } else {
                PickActivity.launch(MainActivity.this, uId, uToken, result.getPickLocation());
                MainActivity.this.finish();
            }
        }


        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            MainActivity.this.finish();
        }

        @Override
        public void netNull() {
            super.netNull();
            MainActivity.this.finish();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            MainActivity.this.finish();
        }
    }
}
