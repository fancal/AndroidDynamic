package com.elianshang.wms.app.createtransfer.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.createtransfer.bean.LocationView;

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
            String uomQty = optString(data, "uomQty");

            if (!TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(itemName)
                    && !TextUtils.isEmpty(packName)
                    && !TextUtils.isEmpty(itemId)
                    && !TextUtils.isEmpty(uomQty)) {

                locationView = new LocationView();
                locationView.setLocationCode(locationCode);
                locationView.setItemId(itemId);
                locationView.setItemName(itemName);
                locationView.setPackName(packName);
                locationView.setLotId(lotId);
                locationView.setUomQty(uomQty);
            }
        }

        return locationView;
    }
}
