package com.elianshang.wms.app.shelve.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.Shelve;

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
        return shelve;
    }
}
