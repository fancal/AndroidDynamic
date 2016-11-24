package com.elianshang.wms.app.pick.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.pick.bean.Split;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/11/24.
 */

public class SplitParser extends MasterParser<Split> {

    @Override
    public Split parse(JSONObject data) throws Exception {
        if (data != null) {
            Split split = new Split();
            split.setTaskId(optString(data, "taskId"));

            return split;
        }
        return null;
    }
}
