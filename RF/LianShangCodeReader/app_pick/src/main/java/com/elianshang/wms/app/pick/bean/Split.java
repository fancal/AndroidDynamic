package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/11/24.
 */

public class Split implements BaseBean {

    private String taskId;

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
