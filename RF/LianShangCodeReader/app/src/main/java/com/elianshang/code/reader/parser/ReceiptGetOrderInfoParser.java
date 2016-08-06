package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.ReceiptInfo;

import org.json.JSONObject;

public class ReceiptGetOrderInfoParser extends MasterParser<ReceiptInfo> {

    @Override
    public ReceiptInfo parse(JSONObject data) throws Exception {

        ReceiptInfo info = new ReceiptInfo();
        info.setSkuName(getString(data, "skuName"));
        info.setOrderQty(getInt(data, "orderQty"));
        info.setPackUnit(getString(data, "packUnit"));
        info.setBatchNeeded(getInt(data, "batchNeeded"));
        return info;
    }
}
