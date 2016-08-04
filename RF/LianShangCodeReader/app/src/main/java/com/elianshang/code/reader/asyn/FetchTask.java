package com.elianshang.code.reader.asyn;

import android.content.Context;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.bean.Task;
import com.elianshang.code.reader.http.HttpApi;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 领取移库任务
 */
public class FetchTask extends HttpAsyncTask<Task> {

    public FetchTask(Context context) {
        super(context);
    }

    @Override
    public DataHull<Task> doInBackground() {
        return HttpApi.procurementFetchTask("19", BaseApplication.get().getUserId());
    }

    @Override
    public void onPostExecute(int updateId, Task result) {

    }

    public interface CallBack {

        void success(String taskId);

        void failed(String errStr);

    }
}
