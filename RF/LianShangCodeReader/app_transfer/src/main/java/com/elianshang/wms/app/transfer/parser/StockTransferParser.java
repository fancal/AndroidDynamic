package com.elianshang.wms.app.transfer.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.transfer.bean.Transfer;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class StockTransferParser extends MasterParser<Transfer> {

    @Override
    public Transfer parse(JSONObject data) throws Exception {
        Transfer transfer = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String type = optString(data, "type");
            String subType = optString(data, "subType");
            String locationCode = optString(data, "locationCode");
            String itemId = optString(data, "itemId");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String uomQty = optString(data, "uomQty");
            String owner = optString(data, "owner");
            String barcode = optString(data, "barcode");
            String skuCode = optString(data, "skuCode");

            if (!TextUtils.isEmpty(taskId)
                    && !TextUtils.isEmpty(type)
                    && !TextUtils.isEmpty(subType)) {
                transfer = new Transfer();
                transfer.setTaskId(taskId);
                transfer.setType(type);
                transfer.setSubType(subType);
                transfer.setLocationCode(locationCode);
                transfer.setItemId(itemId);
                transfer.setItemName(itemName);
                transfer.setPackName(packName);
                transfer.setUomQty(uomQty);
                transfer.setOwner(owner);
                transfer.setBarCode(barcode);
                transfer.setSkuCode(skuCode);
            }
        }
        return transfer;
    }
}
