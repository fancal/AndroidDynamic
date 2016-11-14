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
            String storeName = optString(data, "storeName");
            String qty = optString(data, "qty");
            String packName = optString(data, "packName");
            String skuName = optString(data, "skuName");
            String storeNo = optString(data, "storeNo");

            if (!TextUtils.isEmpty(taskId)) {
                sow = new Sow();

                sow.setTaskId(taskId);
                sow.setStoreNo(storeNo);
                sow.setStoreName(storeName);
                sow.setQty(qty);
                sow.setPackName(packName);
                sow.setSkuName(skuName);
            }
        }
        return sow;
    }
}
