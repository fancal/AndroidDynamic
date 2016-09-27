package com.elianshang.wms.app.sow.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.sow.bean.Sow;

import org.json.JSONObject;

public class SowParser extends MasterParser<Sow> {

    @Override
    public Sow parse(JSONObject data) throws Exception {
        Sow sow = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String containerId = optString(data, "containerId");
            String qty = optString(data, "qty");
            String packName = optString(data, "packName");
            String itemName = optString(data, "skuName");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(containerId)
                    && !TextUtils.isEmpty(qty)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(itemName)) {
                sow = new Sow();

                sow.setTaskId(taskId);
                sow.setContainerId(containerId);
                sow.setQty(qty);
                sow.setPackName(packName);
                sow.setItemName(itemName);
            }
        }
        return sow;
    }
}
