package com.elianshang.wms.app.releasecollection.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.releasecollection.bean.CollectionRoadDetail;

import org.json.JSONObject;

public class CollectionRoadDetailParser extends MasterParser<CollectionRoadDetail> {

    @Override
    public CollectionRoadDetail parse(JSONObject data) throws Exception {
        if (data != null) {
            String customerCount = optString(data, "customerCount");
            String packCount = optString(data, "packCount");
            String turnoverBoxNum = optString(data, "turnoverBoxNum");
            String transPlan = optString(data, "transPlan");
            String driverName = optString(data, "driverName");
            String carNumber = optString(data, "carNumber");


            CollectionRoadDetail detail = new CollectionRoadDetail();

            detail.setCustomerCount(customerCount);
            detail.setPackCount(packCount);
            detail.setTurnoverBoxNum(turnoverBoxNum);
            detail.setTransPlan(transPlan);
            detail.setDriverName(driverName);
            detail.setCarNumber(carNumber);

            return detail;
        }
        return null;
    }
}
