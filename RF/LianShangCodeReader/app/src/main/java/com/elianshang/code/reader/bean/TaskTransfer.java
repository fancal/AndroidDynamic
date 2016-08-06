package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/4.
 */
public class TaskTransfer implements BaseBean {

    String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
