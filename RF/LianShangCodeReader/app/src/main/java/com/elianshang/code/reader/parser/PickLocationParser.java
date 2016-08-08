package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Pick;
import com.elianshang.code.reader.bean.PickLocation;

import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class PickLocationParser extends MasterParser<PickLocation> {
    @Override
    public PickLocation parse(JSONObject data) throws Exception {
        PickLocation bean = null;
        if (data != null) {
            bean = new PickLocation();
            JSONObject nextJson = optJSONObject(data, "next_detail");
            if (nextJson != null) {
                Pick pick = new Pick();
                pick.setItemId(optString(nextJson, "itemId"));
                pick.setAllocPickLocation(optString(nextJson, "allocPickLocation"));
                pick.setAllocCollectLocation(optString(data, "allocCollectLocation"));
                pick.setAllocQty(optString(nextJson, "allocQty"));
                bean.setPick(pick);
            }
            bean.setDone(optBoolean(data, "done"));
        }
        return bean;
    }
}
