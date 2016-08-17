package com.elianshang.bridge.asyn;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseTaskImpl implements BaseTask {

    /**
     * 是否取消加载
     */
    protected boolean isCancel = false;

    /**
     * 线程池
     */
    protected static final ExecutorService mThreadPool;

    static {// 初始化线程池
        mThreadPool = Executors.newFixedThreadPool(10, Executors.defaultThreadFactory());
    }

    @Override
    public void cancel() {
        this.isCancel = true;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancel;
    }

    @Override
    public int compareTo(BaseTask another) {
        return 0;
    }
}
