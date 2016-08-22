package com.elianshang.wms.app.transfer.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.transfer.bean.StockTransfer;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class StockTransferParser extends MasterParser<StockTransfer> {

    @Override
    public StockTransfer parse(JSONObject data) throws Exception {
        StockTransfer stockTransfer = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String type = optString(data, "type");
            String locationCode = optString(data, "locationCode");
            String locationId = optString(data, "locationId");
            String itemId = optString(data, "itemId");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String uomQty = optString(data, "uomQty");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(type)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(locationId)
                    && !TextUtils.isEmpty(itemId)
                    && !TextUtils.isEmpty(itemName)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(uomQty)) {
                stockTransfer = new StockTransfer();
                stockTransfer.setTaskId(taskId);
                stockTransfer.setType(type);
                stockTransfer.setLocationCode(locationCode);
                stockTransfer.setLocationId(locationId);
                stockTransfer.setItemId(itemId);
                stockTransfer.setItemName(itemName);
                stockTransfer.setPackName(packName);
                stockTransfer.setUomQty(uomQty);
            }
        }
        return stockTransfer;
    }
}
