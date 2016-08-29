package com.elianshang.wms.app.procurement.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.provider.FetchTaskProvider;
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

    private class FetchProcurementTask extends HttpAsyncTask<Procurement> {

        private String uId;

        private String uToken;

        public FetchProcurementTask(String uId, String uToken) {
            super(MainActivity.this.that, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<Procurement> doInBackground() {
            return FetchTaskProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(Procurement result) {
            ProcurementActivity.launch(MainActivity.this, uId, uToken, result);
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
