package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Shelve;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/3.
 */
public class ShelveParser extends MasterParser<Shelve> {

    @Override
    public Shelve parse(JSONObject data) throws Exception {
        Shelve shelve = new Shelve();
        shelve.setTaskId(getString(data, "taskId"));
        shelve.setId(getString(data, "id"));
        shelve.setReceiptId(getString(data, "receiptId"));
        shelve.setOrderId(getString(data, "orderId"));
        shelve.setSkuId(getString(data, "skuId"));
        shelve.setOwnerId(getString(data, "ownerId"));
        shelve.setLotId(getString(data, "lotId"));
        shelve.setSupplierId(getString(data, "supplierId"));
        shelve.setAllocLocationId(getString(data, "allocLocationId"));
        shelve.setRealLocationId(getString(data, "realLocationId"));
        shelve.setContainerId(getString(data, "containerId"));
        return shelve;
    }
}
