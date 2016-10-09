package com.elianshang.wms.app.pickup.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.pickup.bean.PickUpView;

import org.json.JSONObject;

public class PickUpViewParser extends MasterParser<PickUpView> {
    @Override
    public PickUpView parse(JSONObject data) throws Exception {
        PickUpView pickUpView = null;
        if (data != null) {
            String containerId = optString(data, "containerId");
            String locationCode = optString(data, "locationCode");
            String status = optString(data, "status");

            if (!TextUtils.isEmpty(containerId)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(status)) {
                pickUpView = new PickUpView();
                pickUpView.setContainerId(containerId);
                pickUpView.setLocationCode(locationCode);
                pickUpView.setStatus(status);
            }
        }
        return pickUpView;
    }
}
