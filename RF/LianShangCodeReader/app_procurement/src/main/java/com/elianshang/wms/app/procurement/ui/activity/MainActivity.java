package com.elianshang.wms.app.procurement.ui.activity;

import android.os.Bundle;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.wms.app.procurement.bean.TaskTransfer;
import com.elianshang.wms.app.takestock.provider.FetchTaskProvider;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.xue.http.impl.DataHull;

public class MainActivity extends DLBasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uId = getIntent().getStringExtra("uId");
        String uToken = getIntent().getStringExtra("uToken");

        new FetchProcurementTask(uId, uToken).start();
    }


    /**
     * Created by liuhanzhi on 16/8/3. 领取补货任务
     */
    private class FetchProcurementTask extends HttpAsyncTask<TaskTransfer> {

        private String uId;

        private String uToken;

        public FetchProcurementTask(String uId, String uToken) {
            super(MainActivity.this.that, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<TaskTransfer> doInBackground() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return FetchTaskProvider.request("",uId);
        }

        @Override
        public void onPostExecute(int updateId, TaskTransfer result) {
            ProcurementActivity.launch(MainActivity.this, uId, uToken, result.getTaskId());
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
