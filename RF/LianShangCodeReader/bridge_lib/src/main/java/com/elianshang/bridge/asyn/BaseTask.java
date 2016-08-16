package com.elianshang.bridge.asyn;

/**
 * 任务接口，描述基本任务属性与结构
 */
public interface BaseTask extends Comparable<BaseTask>, Runnable {

    /**
     * 成功
     */
    public int SUCCESS = 1;

    /**
     * 失败
     */
    public int FAIL = 2;

//    /**
//     * 执行方法，执行业务代码，并给出执行结果
//     */
//    public void run();

    /**
     * 终止，非异常终止，存在延时
     */
    public void cancel();

    /**
     * 是否取消
     */
    public boolean isCancelled();
}
