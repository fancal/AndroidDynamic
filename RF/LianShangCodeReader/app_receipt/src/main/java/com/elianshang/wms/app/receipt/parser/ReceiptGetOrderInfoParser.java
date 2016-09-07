package com.elianshang.wms.app.receipt.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.Info;

import org.json.JSONObject;

public class ReceiptGetOrderInfoParser extends MasterParser<Info> {

    @Override
    public Info parse(JSONObject data) throws Exception {
        Info info = null;

        if (data != null) {
            String skuName = optString(data, "skuName");
            String orderQty = optString(data, "orderQty");
            String packName = optString(data, "packName");
            int batchNeeded = optInt(data, "batchNeeded");

            packName = "服务器问题,提醒他们改";

            if (!TextUtils.isEmpty(skuName)
                    && !TextUtils.isEmpty(orderQty)
                    && !TextUtils.isEmpty(packName)
                    && batchNeeded != -1) {
                info = new Info();

                info.setSkuName(skuName);
                info.setOrderQty(orderQty);
                info.setPackName(packName);
                info.setBatchNeeded(batchNeeded);
            }
        }

        return info;
    }
}
