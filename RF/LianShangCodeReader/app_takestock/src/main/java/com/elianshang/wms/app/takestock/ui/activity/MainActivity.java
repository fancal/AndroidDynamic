package com.elianshang.wms.app.takestock.ui.activity;

import android.os.Bundle;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.StockTakingProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uId = getIntent().getStringExtra("uId");
        String uToken = getIntent().getStringExtra("uToken");

        new StockTakingAssignTask(uId, uToken).start();
    }


    /**
     * 领取盘点任务
     */
    private class StockTakingAssignTask extends HttpAsyncTask<TakeStockList> {

        private String uid;

        private String uToken;

        public StockTakingAssignTask(String uid, String uToken) {
            super(MainActivity.this.that, true, true, false);
            this.uid = uid;
            this.uToken = uToken;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return StockTakingProvider.request(uid);
        }

        @Override
        public void onPostExecute(int updateId, TakeStockList result) {
            TakeStockActivity.launch(MainActivity.this, uid, uToken, result);
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
