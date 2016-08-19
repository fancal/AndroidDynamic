package com.elianshang.wms.app.receipt.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.ReceiptInfo;

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
