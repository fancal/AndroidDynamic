package com.elianshang.wms.app.setofgoods.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.setofgoods.bean.SetOfGoodsView;

import org.json.JSONObject;

public class SetOfGoodsViewParser extends MasterParser<SetOfGoodsView> {
    @Override
    public SetOfGoodsView parse(JSONObject data) throws Exception {
        SetOfGoodsView setOfGoodsView = null;
        if (data != null) {
            String containerId = optString(data, "containerId");
            String locationCode = optString(data, "locationCode");
            String status = optString(data, "status");
            String storeName = optString(data, "storeName");
            String storeNo = optString(data, "storeNo");

            if (!TextUtils.isEmpty(containerId)
                    && !TextUtils.isEmpty(locationCode)
                    && !TextUtils.isEmpty(status)) {
                setOfGoodsView = new SetOfGoodsView();
                setOfGoodsView.setContainerId(containerId);
                setOfGoodsView.setLocationCode(locationCode);
                setOfGoodsView.setStatus(status);
                setOfGoodsView.setStoreName(storeName);
                setOfGoodsView.setStoreNo(storeNo);
            }
        }
        return setOfGoodsView;
    }
}
