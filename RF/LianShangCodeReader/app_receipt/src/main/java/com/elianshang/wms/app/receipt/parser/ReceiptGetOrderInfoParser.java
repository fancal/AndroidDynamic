package com.elianshang.wms.app.receipt.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.Info;

import org.json.JSONObject;

public class ReceiptGetOrderInfoParser extends MasterParser<Info> {

    @Override
    public Info parse(JSONObject data) throws Exception {

        Info info = new Info();
        info.setSkuName(getString(data, "skuName"));
        info.setOrderQty(getString(data, "orderQty"));
        info.setPackUnit(getString(data, "packUnit"));
        info.setBatchNeeded(getInt(data, "batchNeeded"));
        return info;
    }
}
