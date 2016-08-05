package com.elianshang.code.reader.parser;

import android.text.TextUtils;

import com.elianshang.code.reader.bean.TakeStockDetail;

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
            String locationId = optString(data, "locationId");
            String itemName = optString(data, "itemName");
            String itemUnit = optString(data, "itemUnit");
            int qty = optInt(data, "qty");

            if (!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(itemUnit) && qty > 0) {
                TakeStockDetail detail = new TakeStockDetail();
                detail.setViewType(viewType);
                detail.setTaskId(taskId);
                detail.setLocationId(locationId);
                detail.setItemName(itemName);
                detail.setItemUnit(itemUnit);
                detail.setQty(qty);

                return detail;
            }
        }
        return null;
    }
}
