package com.elianshang.wms.app.takestock.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.AssignProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    private String uId;

    private String uToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new StockTakingAssignTask(uId, uToken).start();
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
    private class StockTakingAssignTask extends HttpAsyncTask<TakeStockList> {

        private String uId;

        private String uToken;

        public StockTakingAssignTask(String uid, String uToken) {
            super(MainActivity.this.that, true, true, false);
            this.uId = uid;
            this.uToken = uToken;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return AssignProvider.request(uId, uToken);
        }

        @Override
        public void onPostExecute(TakeStockList result) {
            TakeStockActivity.launch(MainActivity.this, uId, uToken, result);
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
