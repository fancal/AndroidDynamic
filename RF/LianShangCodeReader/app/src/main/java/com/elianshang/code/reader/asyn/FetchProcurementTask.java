package com.elianshang.code.reader.asyn;

import android.content.Context;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.bean.TaskTransfer;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.activity.ProcurementActivity;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 领取补货任务
 */
public class FetchProcurementTask extends HttpAsyncTask<TaskTransferDetail> {

    public FetchProcurementTask(Context context) {
        super(context, true, true, false);
    }

    @Override
    public DataHull<TaskTransferDetail> doInBackground() {
        DataHull<TaskTransfer> dataHull = HttpApi.procurementFetchTask("", BaseApplication.get().getUserId());
        if (dataHull != null && dataHull.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
            TaskTransfer taskTransfer = dataHull.getDataEntity();
            DataHull<TaskTransferDetail> dataHull1 = HttpApi.procurementView(taskTransfer.getTaskId());
            if (dataHull1 != null && dataHull1.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                dataHull1.getDataEntity().setTaskId(taskTransfer.getTaskId());
                return dataHull1;
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(int updateId, TaskTransferDetail result) {
        ProcurementActivity.launch(context, result, true);
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        ToastTool.show(context, "没有补货任务");
    }

//    public interface CallBack {
//
//        void success(String taskId);
//
//        void failed(String errStr);
//
//    }
}
