package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/4.
 */
public class TaskDetail implements BaseBean {

    String taskId;

    String InLocationId;

    String outLocationId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInLocationId() {
        return InLocationId;
    }

    public void setInLocationId(String inLocationId) {
        InLocationId = inLocationId;
    }

    public String getOutLocationId() {
        return outLocationId;
    }

    public void setOutLocationId(String outLocationId) {
        this.outLocationId = outLocationId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
