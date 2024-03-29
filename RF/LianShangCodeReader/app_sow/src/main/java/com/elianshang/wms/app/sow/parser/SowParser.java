package com.elianshang.wms.app.sow.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.sow.bean.Sow;

import org.json.JSONObject;

public class SowParser extends MasterParser<Sow> {

    @Override
    public Sow parse(JSONObject data) throws Exception {
        Sow sow = null;
        if (data != null) {
            String taskId = optString(data, "taskId");
            String customerName = optString(data, "customerName");
            String qty = optString(data, "qty");
            String packName = optString(data, "packName");
            String skuName = optString(data, "skuName");
            String customerCode = optString(data, "customerCode");
            String barcode = optString(data, "barcode");
            String skuCode = optString(data, "skuCode");

            if (!TextUtils.isEmpty(taskId)) {
                sow = new Sow();

                sow.setTaskId(taskId);
                sow.setCustomerCode(customerCode);
                sow.setCustomerName(customerName);
                sow.setQty(qty);
                sow.setPackName(packName);
                sow.setSkuName(skuName);
                sow.setBarcode(barcode);
                sow.setSkuCode(skuCode);
            }
        }
        return sow;
    }
}
