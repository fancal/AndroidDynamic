package com.elianshang.wms.app.transfer.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.transfer.bean.LocationView;

import org.json.JSONObject;

public class LocationViewParser extends MasterParser<LocationView> {

    @Override
    public LocationView parse(JSONObject data) throws Exception {
        LocationView locationView = null;
        if (data != null) {
            String locationCode = optString(data, "locationCode");
            String itemName = optString(data, "itemName");
            String lotId = optString(data, "lotId");
            String packName = optString(data, "packName");
            String itemId = optString(data, "itemId");
            String barcode = optString(data, "barcode");
            String uom = optString(data, "uom");
            String uomQty = optString(data, "uomQty");

            if (!TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(itemName)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(barcode)
                    && !TextUtils.isEmpty(uom)
                    && !TextUtils.isEmpty(itemId)
                    && !TextUtils.isEmpty(uomQty)) {

                locationView = new LocationView();
                locationView.setLocationCode(locationCode);
                locationView.setItemId(itemId);
                locationView.setItemName(itemName);
                locationView.setPackName(packName);
                locationView.setLotId(lotId);
                locationView.setUomQty(uomQty);
                locationView.setBarCode(barcode);
                locationView.setUom(uom);
            }
        }

        return locationView;
    }
}
