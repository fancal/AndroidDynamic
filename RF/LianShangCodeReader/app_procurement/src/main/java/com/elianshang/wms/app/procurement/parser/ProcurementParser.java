package com.elianshang.wms.app.procurement.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.Procurement;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class ProcurementParser extends MasterParser<Procurement> {

    @Override
    public Procurement parse(JSONObject data) throws Exception {
        Procurement procurement = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String type = optString(data, "type");
            String subType = optString(data, "subType");
            String locationCode = optString(data, "locationCode");
            String locationId = optString(data, "locationId");
            String itemId = optString(data, "itemId");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String uomQty = optString(data, "uomQty");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(type)
                    && !TextUtils.isEmpty(subType)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(locationId)
                    && !TextUtils.isEmpty(itemId)
                    && !TextUtils.isEmpty(itemName)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(uomQty)) {
                procurement = new Procurement();
                procurement.setTaskId(taskId);
                procurement.setType(type);
                procurement.setSubType(subType);
                procurement.setLocationCode(locationCode);
                procurement.setLocationId(locationId);
                procurement.setItemId(itemId);
                procurement.setItemName(itemName);
                procurement.setPackName(packName);
                procurement.setUomQty(uomQty);
            }
        }
        return procurement;
    }
}
