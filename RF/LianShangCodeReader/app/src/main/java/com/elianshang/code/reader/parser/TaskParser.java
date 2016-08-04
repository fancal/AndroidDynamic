package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Task;

import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/4.
 */
public class TaskParser extends MasterParser<Task> {
    @Override
    public Task parse(JSONObject data) throws Exception {
        Task task = null;
        if (data != null) {
            task = new Task();
            task.setTaskId(optString(data, "taskId"));
        }
        return task;
    }
}
