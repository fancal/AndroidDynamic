package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by wangwenwang on 16/8/3.
 */
public class ReceiveTaskActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ReceiveTaskActivity.class);
        context.startActivity(intent);
    }


    private ScanEditText containerEditText;
    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_task);

        containerEditText = (ScanEditText) findViewById(R.id.container_id);

        scanEditTextTool = new ScanEditTextTool(this, containerEditText);
        scanEditTextTool.setComplete(this);

//        new RequestShelveCreateTask(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);

    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onSetComplete() {
        new RequestShelveScanTask(this).start();
    }

    @Override
    public void onInputError(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class RequestShelveCreateTask extends HttpAsyncTask<User> {

        public RequestShelveCreateTask(Context context) {
            super(context, true, true);
        }

        @Override
        public DataHull<User> doInBackground() {
            DataHull<User> dataHull = HttpApi.shelveCreateTask("103", "53738630820595");
            return dataHull;
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            BaseApplication.get().setUser(result);
            finish();
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }


    private class RequestShelveScanTask extends HttpAsyncTask<User> {

        public RequestShelveScanTask(Context context) {
            super(context, true, true);
        }

        @Override
        public DataHull<User> doInBackground() {
            DataHull<User> dataHull = HttpApi.shelveScanContainer("103", "143280109020606", "53738630820595");
            return dataHull;
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            FinishOperationTaskActivity.launch(ReceiveTaskActivity.this, "taskId", "allocLocationId");
            finish();
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }
}
