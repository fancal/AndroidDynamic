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
            String locationId = optString(data, "locationCode");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String qty = optString(data, "qty");

            if (!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(locationId)) {
                TakeStockDetail detail = new TakeStockDetail();
                detail.setViewType(viewType);
                detail.setTaskId(taskId);
                detail.setLocationId(locationId);
                detail.setItemName(itemName);
                detail.setPackName(packName);
                detail.setQty(qty);

                return detail;
            }
        }
        return null;
    }
}
