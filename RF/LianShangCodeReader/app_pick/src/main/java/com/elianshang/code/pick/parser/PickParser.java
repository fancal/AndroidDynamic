package com.elianshang.code.pick.parser;


import com.elianshang.code.pick.bean.Pick;
import com.elianshang.bridge.parser.MasterParser;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class PickParser extends MasterParser<Pick> {
    @Override
    public Pick parse(JSONObject data) throws Exception {
        Pick pick = null;
        JSONArray jsonArray = optJSONArray(data, "pick_details");
        int length = getLength(jsonArray);
        if (length > 0) {
            data = optJSONObject(jsonArray, 0);
            if (data != null) {
                pick = new Pick();
                pick.setItemId(optString(data, "itemId"));
                pick.setAllocPickLocationCode(optString(data, "allocPickLocation"));
                pick.setAllocCollectLocationCode(optString(data, "allocCollectLocation"));
                pick.setAllocQty(optString(data, "allocQty"));
            }
        }
        return pick;
    }

}
