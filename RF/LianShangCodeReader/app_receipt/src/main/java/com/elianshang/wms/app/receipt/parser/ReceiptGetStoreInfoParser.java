package com.elianshang.wms.app.receipt.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.StoreReceiptInfo;

import org.json.JSONObject;

public class ReceiptGetStoreInfoParser extends MasterParser<StoreReceiptInfo> {

    @Override
    public StoreReceiptInfo parse(JSONObject data) throws Exception {
        StoreReceiptInfo storeReceiptInfo = null;

        if (data != null) {

            JSONObject object = optJSONObject(data, "receiptInfo");

            if (object != null) {
                String location = optString(object, "location");
                String skuCode = optString(object, "skuCode");
                String barcode = optString(object, "barcode");
                String proTime = optString(object, "proTime");
                String packUnit = optString(object, "packUnit");
                String skuName = optString(object, "skuName");
                String barCode = optString(object, "barCode");
                String packName = optString(object, "packName");
                String orderId = optString(object, "orderId");
                String orderQty = optString(object, "orderQty");
                int isNeedProTime = optInt(object, "isNeedProTime");

                if (!TextUtils.isEmpty(location)
                        && !TextUtils.isEmpty(proTime)
                        && !TextUtils.isEmpty(packUnit)
                        && !TextUtils.isEmpty(skuName)
                        && !TextUtils.isEmpty(barCode)
                        && !TextUtils.isEmpty(packName)
                        && !TextUtils.isEmpty(orderId)
                        && !TextUtils.isEmpty(orderQty)) {
                    storeReceiptInfo = new StoreReceiptInfo();


                    storeReceiptInfo.setBarcode(barcode);
                    storeReceiptInfo.setSkuCode(skuCode);
                    storeReceiptInfo.setLocation(location);
                    storeReceiptInfo.setProTime(proTime);
                    storeReceiptInfo.setPackUnit(packUnit);
                    storeReceiptInfo.setSkuName(skuName);
                    storeReceiptInfo.setBarCode(barCode);
                    storeReceiptInfo.setPackName(packName);
                    storeReceiptInfo.setOrderId(orderId);
                    storeReceiptInfo.setOrderQty(orderQty);
                    storeReceiptInfo.setIsNeedProTime(isNeedProTime);
                }
            }

        }

        return storeReceiptInfo;
    }
}
