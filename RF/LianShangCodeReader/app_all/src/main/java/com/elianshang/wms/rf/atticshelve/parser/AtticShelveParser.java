package com.elianshang.wms.rf.atticshelve.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.rf.atticshelve.bean.AtticShelve;

import org.json.JSONObject;

public class AtticShelveParser extends MasterParser<AtticShelve> {

    @Override
    public AtticShelve parse(JSONObject data) throws Exception {
        AtticShelve atticShelve = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String locationCode = optString(data, "locationCode");
            String qty = optString(data, "qty");
            String packName = optString(data, "packName");
            String itemName = optString(data, "skuName");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(qty)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(itemName)) {
                atticShelve = new AtticShelve();

                atticShelve.setTaskId(taskId);
                atticShelve.setLocationCode(locationCode);
                atticShelve.setQty(qty);
                atticShelve.setPackName(packName);
                atticShelve.setItemName(itemName);
            }
        }
        return atticShelve;
    }
}
