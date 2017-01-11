package com.elianshang.wms.app.takestock.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/4.
 */
public class TakeStockDetailParser extends MasterParser<TakeStockDetail> {

    @Override
    public TakeStockDetail parse(JSONObject data) throws Exception {

        if (data != null) {
            String viewType = optString(data, "viewType");
            String taskId = optString(data, "taskId");
            String locationCode = optString(data, "locationCode");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String qty = optString(data, "qty");
            String barcode = optString(data, "barcode");
            String skuCode = optString(data, "skuCode");
            String packCode = optString(data, "packCode");

            if (!TextUtils.isEmpty(locationCode)) {
                TakeStockDetail detail = new TakeStockDetail();
                detail.setViewType(viewType);
                detail.setTaskId(taskId);
                detail.setLocationCode(locationCode);
                detail.setItemName(itemName);
                detail.setPackName(packName);
                detail.setQty(qty);
                detail.setBarcode(barcode);
                detail.setSkuCode(skuCode);
                detail.setPackCode(packCode);

                return detail;
            }
        }
        return null;
    }
}
