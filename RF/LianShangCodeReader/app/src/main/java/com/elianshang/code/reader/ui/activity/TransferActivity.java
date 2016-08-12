package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.TaskTransfer;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.controller.TransferController;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 移库
 */
public class TransferActivity extends BaseActivity implements ScanManager.OnBarCodeListener {

    public static void launch(Context context) {
        new FetchTransferTask(context).start();
    }

    private static void launchInner(Context context, String taskId) {
        Intent intent = new Intent(context, TransferActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    private TransferController transferController;

    private String taskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readExtras();
        setContentView(R.layout.activity_transfer);
        init();
    }

    private void readExtras() {
        taskId = getIntent().getStringExtra("taskId");
        //FIXME  test
        taskId = "10010";
    }

    private void init() {
        transferController = new TransferController(TransferActivity.this);
        new TransferViewTask(this, taskId).start();
    }

    private void fill(TaskTransferDetail result) {
        transferController.setData(result, taskId);
        transferController.fillLocationLayout();
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
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(this, "任务未完成，确认退出？", "取消", "确认", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }


    @Override
    public void OnBarCodeReceived(String s) {
        transferController.OnBarCodeReceived(s);

    }


    /**
     * Created by liuhanzhi on 16/8/3. 领取移库任务
     */
    private static class FetchTransferTask extends HttpAsyncTask<TaskTransfer> {

        public FetchTransferTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<TaskTransfer> doInBackground() {
            return HttpApi.stockTransferFetchTask("", BaseApplication.get().getUserId());
        }

        @Override
        public void onPostExecute(int updateId, TaskTransfer result) {
            TransferActivity.launchInner(context, result.getTaskId());
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            ToastTool.show(context, "没有移库任务");
        }

    }

    /**
     * Created by liuhanzhi on 16/8/3. 查看任务详情
     */
    private class TransferViewTask extends HttpAsyncTask<TaskTransferDetail> {

        private String taskId;

        public TransferViewTask(Context context, String taskId) {
            super(context, true, true, false);
            this.taskId = taskId;
        }

        @Override
        public DataHull<TaskTransferDetail> doInBackground() {
            return HttpApi.stockTransferView(taskId);
        }

        @Override
        public void onPostExecute(int updateId, TaskTransferDetail result) {
            fill(result);
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            ToastTool.show(context, "没有移库任务");
        }

    }

}
