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
            if (!has(data, "done")) {
                return null;
            }

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
                pick.setAllocUnitName(optString(nextJson, "allocUnitName"));
                pick.setAllocQty(optString(nextJson, "allocQty"));
                pick.setPickTaskId(optString(nextJson, "pickTaskId"));
                pick.setContainerId(optString(nextJson, "containerId"));
                pick.setBarcode(optString(nextJson, "barcode"));
                pick.setSkuCode(optString(nextJson, "skuCode"));
                pick.setPackCode(optString(nextJson, "packCode"));
                pick.setPickOrder(optString(nextJson, "pickOrder"));
                pick.setPickTaskOrder(optString(nextJson, "pickTaskOrder"));
                pick.setCustomerName(optString(nextJson, "customerName"));
                bean.setPick(pick);
            }
        }
        return bean;
    }
}
