package com.elianshang.wms.app.create_return.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.create_return.bean.Item;

import org.json.JSONObject;

public class ItemParser extends MasterParser<Item> {
    @Override
    public Item parse(JSONObject data) throws Exception {
        Item item = null;

        data = optJSONObject(data, "info");
        if (data != null) {
            item = new Item();
            item.setName(optString(data, "name"));
            item.setItemId(optString(data, "itemId"));
            item.setPackName(optString(data, "packName"));
        }
        return item;
    }
}
