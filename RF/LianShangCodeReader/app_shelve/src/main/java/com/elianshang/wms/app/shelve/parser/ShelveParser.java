package com.elianshang.wms.app.shelve.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.Shelve;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShelveParser extends MasterParser<Shelve> {

    @Override
    public Shelve parse(JSONObject data) throws Exception {

        Shelve shelve = new Shelve();
        shelve.setTaskId(optString(data, "taskId"));
        shelve.setId(optString(data, "id"));
        shelve.setReceiptId(optString(data, "receiptId"));
        shelve.setOrderId(optString(data, "orderId"));
        shelve.setSkuId(optString(data, "skuId"));
        shelve.setOwnerId(optString(data, "ownerId"));
        shelve.setLotId(optString(data, "lotId"));
        shelve.setSupplierId(optString(data, "supplierId"));
        shelve.setAllocLocationCode(optString(data, "allocLocationCode"));
        shelve.setContainerId(optString(data, "containerId"));
        shelve.setItemName(optString(data, "skuName"));
        shelve.setBarcode(optString(data, "barcode"));
        shelve.setSkuCode(optString(data, "skuCode"));

        JSONArray array = getJSONArray(data, "pickLocationIdList");
        int len = getLength(array);

        if (len > 0) {
            String ss = "";
            for (int i = 0; i < len; i++) {
                ss += optString(optJSONObject(array, i), "locationCode");
                if (i + 1 < len) {
                    ss += "，";
                }
            }

            shelve.setPickLocationList(ss);
        }

        return shelve;
    }
}
