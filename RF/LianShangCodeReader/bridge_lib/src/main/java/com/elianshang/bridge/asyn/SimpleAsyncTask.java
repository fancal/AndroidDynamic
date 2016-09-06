package com.elianshang.bridge.asyn;

/**
 * 普通异步任务，用来做查询数据库或者读取本地文件并需要更新UI的操作
 */
public abstract class SimpleAsyncTask<T> extends BaseTaskImpl<T, T> {


    @Override
    public void onPreExecute() {
    }

    @Override
    public T run() {
        return doInBackground();
    }
}