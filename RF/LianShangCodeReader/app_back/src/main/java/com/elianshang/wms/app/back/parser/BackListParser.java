package com.elianshang.wms.app.back.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.back.bean.BackList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BackListParser extends MasterParser<BackList> {
    @Override
    public BackList parse(JSONObject data) throws Exception {
        BackList backList = null;
        if (data != null) {
            JSONArray array = optJSONArray(data, "list");
            int len = getLength(array);

            if (len > 0) {
                backList = new BackList();

                for (int i = 0; i < len; i++) {
                    JSONObject object = optJSONObject(array, i);

                    if (object != null) {
                        BackList.Item item = new BackList.Item();

                        item.setItemName(optString(object, "itemName"));
                        item.setBarcode(optString(object, "barcode"));
                        item.setSkuCode(optString(object, "skuCode"));
                        item.setPackCode(optString(object, "packCode"));
                        item.setPackName(optString(object, "packName"));
                        item.setLocationCode(optString(object, "locationCode"));

                        backList.add(item);
                    }
                }
            }
        }

        return backList;
    }
}
