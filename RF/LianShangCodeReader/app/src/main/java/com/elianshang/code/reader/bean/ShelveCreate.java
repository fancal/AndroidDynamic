package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/3.
 */
public class ShelveCreate implements BaseBean {

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setDataKey(String dataKey) {}

    @Override
    public String getDataKey() {
        return null;
    }
}
