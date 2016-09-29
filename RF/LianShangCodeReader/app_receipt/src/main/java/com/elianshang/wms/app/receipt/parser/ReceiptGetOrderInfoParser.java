package com.elianshang.wms.app.receipt.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.OrderReceiptInfo;

import org.json.JSONObject;

public class ReceiptGetOrderInfoParser extends MasterParser<OrderReceiptInfo> {

    @Override
    public OrderReceiptInfo parse(JSONObject data) throws Exception {
        OrderReceiptInfo orderReceiptInfo = null;

        if (data != null) {
            String skuName = optString(data, "skuName");
            String orderQty = optString(data, "orderQty");
            String packName = optString(data, "packName");
            int batchNeeded = optInt(data, "batchNeeded");

            if (!TextUtils.isEmpty(skuName)
                    && !TextUtils.isEmpty(orderQty)
                    && !TextUtils.isEmpty(packName)
                    && batchNeeded != -1) {
                orderReceiptInfo = new OrderReceiptInfo();

                orderReceiptInfo.setSkuName(skuName);
                orderReceiptInfo.setOrderQty(orderQty);
                orderReceiptInfo.setPackName(packName);
                orderReceiptInfo.setBatchNeeded(batchNeeded);
            }
        }

        return orderReceiptInfo;
    }
}
