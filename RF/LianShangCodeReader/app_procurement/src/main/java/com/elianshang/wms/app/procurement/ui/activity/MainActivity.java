package com.elianshang.wms.app.procurement.ui.activity;

import android.os.Bundle;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.provider.FetchTaskProvider;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uId = getIntent().getStringExtra("uId");
        String uToken = getIntent().getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "243202523137671";

        new FetchProcurementTask(uId, uToken).start();
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return FetchTaskProvider.request(uId, uToken);
        }

        @Override
        public void onPostExecute(int updateId, Procurement result) {
            ProcurementActivity.launch(MainActivity.this, uId, uToken, result);
            MainActivity.this.finish();
        }


        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            MainActivity.this.finish();
        }

        @Override
        public void netNull() {
            super.netNull();
            MainActivity.this.finish();
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            MainActivity.this.finish();
        }
    }
}
