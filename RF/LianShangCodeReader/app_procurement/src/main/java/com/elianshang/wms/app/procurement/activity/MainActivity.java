package com.elianshang.wms.app.procurement.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.provider.FetchTaskProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, String zoneId) {
        DLIntent intent = new DLIntent(activity.getPackageName(), MainActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("zoneId", zoneId);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private String zoneId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new FetchProcurementTask(uId, uToken, zoneId).start();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        zoneId = getIntent().getStringExtra("zoneId");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private class FetchProcurementTask extends HttpAsyncTask<Procurement> {

        private String uId;

        private String uToken;

        private String zoneId;

        public FetchProcurementTask(String uId, String uToken, String zoneId) {
            super(MainActivity.this.that, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
            this.zoneId = zoneId;
        }

        @Override
        public DataHull<Procurement> doInBackground() {
            return FetchTaskProvider.request(context, uId, uToken, zoneId);
        }

        @Override
        public void onPostExecute(Procurement result) {
            ProcurementActivity.launch(MainActivity.this, uId, uToken, zoneId, result);
            MainActivity.this.finish();
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
