package com.elianshang.code.reader.asyn;

import android.content.Context;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.bean.TaskTransfer;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.activity.TransferLocationActivity;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 领取移库任务
 */
public class FetchTransferTask extends HttpAsyncTask<TaskTransferDetail> {

    public FetchTransferTask(Context context) {
        super(context, true, true, false);
    }

    @Override
    public DataHull<TaskTransferDetail> doInBackground() {
        DataHull<TaskTransfer> dataHull = HttpApi.stockTransferFetchTask("", BaseApplication.get().getUserId());
        if (dataHull != null && dataHull.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
            TaskTransfer taskTransfer = dataHull.getDataEntity();
            DataHull<TaskTransferDetail> dataHull1 = HttpApi.stockTransferementView(taskTransfer.getTaskId());
            if (dataHull1 != null && dataHull1.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                dataHull1.getDataEntity().setTaskId(taskTransfer.getTaskId());
                return dataHull1;
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(int updateId, TaskTransferDetail result) {
        TransferLocationActivity.launch(context, result, true);
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        ToastTool.show(context, "没有移库任务");
    }

//    public interface CallBack {
//
//        void success(String taskId);
//
//        void failed(String errStr);
//
//    }
}
