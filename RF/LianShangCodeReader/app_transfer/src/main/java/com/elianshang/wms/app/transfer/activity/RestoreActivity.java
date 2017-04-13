package com.elianshang.wms.app.transfer.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.provider.RestoreTaskProvider;
import com.xue.http.impl.DataHull;

public class RestoreActivity extends DLBasePluginActivity {

    private String uId;

    private String uToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new RestoreProcurementTask(uId, uToken).start();
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


    private class RestoreProcurementTask extends HttpAsyncTask<Transfer> {

        private String uId;

        private String uToken;

        public RestoreProcurementTask(String uId, String uToken) {
            super(RestoreActivity.this.that, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<Transfer> doInBackground() {
            return RestoreTaskProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(Transfer result) {
            TransferActivity.launch(RestoreActivity.this, uId, uToken, result);
            RestoreActivity.this.finish();
        }


        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            RestoreActivity.this.finish();
        }

        @Override
        public void netNull() {
            super.netNull();
            RestoreActivity.this.finish();
        }

        @Override
        public void dataNull(String errMsg) {
            ChooseActivity.launch(RestoreActivity.this, uId, uToken);
            RestoreActivity.this.finish();
        }
    }
}
