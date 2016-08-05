package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.TakeStockList;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.BaseActivity;
import com.xue.http.impl.DataHull;

public class TakeStockActivity extends BaseActivity {

    public static void launch(Context context, String uid) {
        RequestFinishOpetationTask task = new RequestFinishOpetationTask(context, uid);
        task.start();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static class RequestFinishOpetationTask extends HttpAsyncTask<TakeStockList> {

        private String uid;

        public RequestFinishOpetationTask(Context context, String uid) {
            super(context, true, true);
            this.uid = uid;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return HttpApi.inhouseStockTakingAssign(uid);
        }

        @Override
        public void onPostExecute(int updateId, TakeStockList result) {
            Intent intent = new Intent(context, TakeStockActivity.class);
            context.startActivity(intent);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }
}
