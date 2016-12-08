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
            String skuCode = optString(data, "skuCode");
            String barcode = optString(data, "barcode");
            String orderQty = optString(data, "orderQty");
            String packName = optString(data, "packName");
            int batchNeeded = optInt(data, "batchNeeded");
            String pile = optString(data, "pile");
            int isNeedProTime = optInt(data, "isNeedProTime");

            if (!TextUtils.isEmpty(skuName)
                    && !TextUtils.isEmpty(orderQty)
                    && !TextUtils.isEmpty(packName)
                    && batchNeeded != -1) {
                orderReceiptInfo = new OrderReceiptInfo();

                orderReceiptInfo.setBarcode(barcode);
                orderReceiptInfo.setSkuCode(skuCode);
                orderReceiptInfo.setSkuName(skuName);
                orderReceiptInfo.setOrderQty(orderQty);
                orderReceiptInfo.setPackName(packName);
                orderReceiptInfo.setBatchNeeded(batchNeeded);
                orderReceiptInfo.setPile(pile);
                orderReceiptInfo.setIsNeedProTime(isNeedProTime);
            }
        }

        return orderReceiptInfo;
    }
}
