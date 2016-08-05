package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.TaskTransfer;

import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/4.
 */
public class TaskTransferParser extends MasterParser<TaskTransfer> {
    @Override
    public TaskTransfer parse(JSONObject data) throws Exception {
        TaskTransfer taskTransfer = null;
        if (data != null) {
            taskTransfer = new TaskTransfer();
            taskTransfer.setTaskId(optString(data, "taskId"));
        }
        return taskTransfer;
    }
}
