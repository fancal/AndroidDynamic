package com.elianshang.wms.app.receipt.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.StoreReceiptInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptGetStoreInfoParser extends MasterParser<StoreReceiptInfo> {

    @Override
    public StoreReceiptInfo parse(JSONObject data) throws Exception {
        StoreReceiptInfo storeReceiptInfo = null;

        if (data != null) {

            JSONObject object = optJSONObject(data, "receiptInfo");

            if (object != null) {
                String proTime = optString(object, "proTime");
                String sumUnitQty = optString(object, "sumUnitQty");
                String skuName = optString(object, "skuName");
                String barCode = optString(object, "barCode");
                String orderIds = optString(object, "orderIds");
                String packName = optString(object, "packName");
                String sumPackQty = optString(object, "sumPackQty");

                if (!TextUtils.isEmpty(proTime)
                        && !TextUtils.isEmpty(sumUnitQty)
                        && !TextUtils.isEmpty(skuName)
                        && !TextUtils.isEmpty(barCode)
                        && !TextUtils.isEmpty(orderIds)
                        && !TextUtils.isEmpty(packName)
                        && !TextUtils.isEmpty(sumPackQty)) {
                    storeReceiptInfo = new StoreReceiptInfo();

                    storeReceiptInfo.setProTime(proTime);
                    storeReceiptInfo.setSumUnitQty(sumUnitQty);
                    storeReceiptInfo.setSkuName(skuName);
                    storeReceiptInfo.setBarCode(barCode);
                    storeReceiptInfo.setOrderIds(orderIds);
                    storeReceiptInfo.setPackName(packName);
                    storeReceiptInfo.setSumPackQty(sumPackQty);


                    JSONArray array = optJSONArray(data, "orderInfo");
                    int len = getLength(array);
                    if (len > 0) {
                        ArrayList<StoreReceiptInfo.OrderInfo> orderInfos = new ArrayList();
                        for (int i = 0; i < len; i++) {
                            object = optJSONObject(array, i);
                            if (object != null) {
                                StoreReceiptInfo.OrderInfo orderInfo = new StoreReceiptInfo.OrderInfo();

                                orderInfo.setCreateTime(optString(object, "createTime"));
                                orderInfo.setObdQty(optString(object, "obdQty"));
                                orderInfo.setOrderId(optString(object, "orderId"));
                                orderInfo.setPackName(optString(object, "packName"));

                                orderInfos.add(orderInfo);
                            }
                        }

                        storeReceiptInfo.setOrderInfos(orderInfos);
                    }

                }
            }

        }

        return storeReceiptInfo;
    }
}
