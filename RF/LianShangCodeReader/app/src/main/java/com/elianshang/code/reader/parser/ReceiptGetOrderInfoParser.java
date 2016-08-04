package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.ReceiptGetOrderInfo;

import org.json.JSONObject;

public class ReceiptGetOrderInfoParser extends MasterParser<ReceiptGetOrderInfo> {

    @Override
    public ReceiptGetOrderInfo parse(JSONObject data) throws Exception {

        ReceiptGetOrderInfo info = new ReceiptGetOrderInfo();
        info.setSkuName(getString(data, "skuName"));
        info.setOrderQty(getInt(data, "orderQty"));
        info.setPackUnit(getString(data, "packUnit"));
        info.setBatchNeeded(getInt(data, "batchNeeded"));
        return info;
    }
}
