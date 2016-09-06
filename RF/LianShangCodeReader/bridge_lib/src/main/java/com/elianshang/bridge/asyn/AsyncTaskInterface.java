package com.elianshang.bridge.asyn;

/**
 * 异步任务接口
 */
public interface AsyncTaskInterface<D, R> {

    void onCancelled();

    /**
     * 异步任务开始前
     */
    void onPreExecute();

    /**
     * 异步任务执行
     */
    D doInBackground();

    /**
     * 异步任务完成
     */
    void onPostExecute(R result);

}
