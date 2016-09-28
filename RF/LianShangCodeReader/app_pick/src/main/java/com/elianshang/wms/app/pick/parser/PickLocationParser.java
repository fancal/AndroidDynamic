package com.elianshang.wms.app.pick.parser;

import com.elianshang.wms.app.pick.bean.Pick;
import com.elianshang.wms.app.pick.bean.PickLocation;
import com.elianshang.bridge.parser.MasterParser;

import org.json.JSONObject;

public class PickLocationParser extends MasterParser<PickLocation> {
    @Override
    public PickLocation parse(JSONObject data) throws Exception {
        PickLocation bean = null;
        if (data != null) {
            bean = new PickLocation();
            JSONObject nextJson;
            bean.setDone(optBoolean(data, "done"));
            bean.setPickDone(optBoolean(data, "pick_done"));

            nextJson = optJSONObject(data, "next_detail");
            if (nextJson != null) {
                Pick pick = new Pick();
                pick.setItemId(optString(nextJson, "itemId"));
                pick.setAllocPickLocationCode(optString(nextJson, "allocPickLocationCode"));
                pick.setAllocCollectLocationCode(optString(nextJson, "allocCollectLocationCode"));
                pick.setItemName(optString(nextJson, "skuName"));
                pick.setPackName(optString(nextJson, "unitName"));
                pick.setAllocQty(optString(nextJson, "allocQty"));
                bean.setPick(pick);
            }
        }
        return bean;
    }
}