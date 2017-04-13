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
            String fromLocationCode = optString(data, "fromLocationCode");
            String toLocationCode = optString(data, "toLocationCode");
            String itemId = optString(data, "itemId");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String qty = optString(data, "qty");
            String remainQty = optString(data, "remainQty");
            String barcode = optString(data, "barcode");
            String skuCode = optString(data, "skuCode");
            String isFlashBack = optString(data, "isFlashBack");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(type)
                    && !TextUtils.isEmpty(subType)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(itemId)
                    && !TextUtils.isEmpty(itemName)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(qty)) {
                procurement = new Procurement();
                procurement.setTaskId(taskId);
                procurement.setType(type);
                procurement.setSubType(subType);
                procurement.setLocationCode(locationCode);
                procurement.setToLocationCode(toLocationCode);
                procurement.setFromLocationCode(fromLocationCode);
                procurement.setItemId(itemId);
                procurement.setItemName(itemName);
                procurement.setPackName(packName);
                procurement.setQty(qty);
                procurement.setRemainQty(remainQty);
                procurement.setBarcode(barcode);
                procurement.setSkuCode(skuCode);
                procurement.setIsFlashBack(isFlashBack);
            }
        }
        return procurement;
    }
}
