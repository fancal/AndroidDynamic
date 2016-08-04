package com.elianshang.code.reader.asyn;

import android.content.Context;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/4. 移库
 */
public class TransferLocationTask extends HttpAsyncTask<User> {

    private String taskId;
    private String locationId;
    private String itemId;
    private String packName;
    private String qty;
    private CallBack callBack;
    private boolean isTransferIn;

    /**
     * @param context
     * @param taskId
     * @param locationId
     * @param itemId
     * @param packName
     * @param qty
     * @param callBack
     * @param isTransferIn
     */
    public TransferLocationTask(Context context, String taskId, String locationId, String itemId, String packName, String qty, CallBack callBack, boolean isTransferIn) {
        super(context, true, true, false);
        this.taskId = taskId;
        this.locationId = locationId;
        this.itemId = itemId;
        this.packName = packName;
        this.qty = qty;
        this.callBack = callBack;
        this.isTransferIn = isTransferIn;
    }

    @Override
    public DataHull<User> doInBackground() {
        if (isTransferIn) {
            return HttpApi.procurementScanFromLocation(taskId, locationId, BaseApplication.get().getUserId(), qty, packName);
        } else {
            return HttpApi.procurementScanToLocation(taskId, locationId, BaseApplication.get().getUserId(), qty, packName);
        }
    }

    @Override
    public void onPostExecute(int updateId, User result) {
        if (callBack != null) {
            callBack.success();
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        super.dataNull(updateId, errMsg);
        if (callBack != null) {
            callBack.failed(errMsg);
        }
    }

    @Override
    public void netNull() {
        super.netNull();
        if (callBack != null) {
            callBack.failed("网络异常");
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        super.netErr(updateId, errMsg);
        if (callBack != null) {
            callBack.failed(errMsg);
        }
    }

    public interface CallBack {

        void success();

        void failed(String errStr);

    }
}
