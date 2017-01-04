package com.elianshang.wms.app.releasecollection.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.releasecollection.bean.CollectionRoadDetail;

import org.json.JSONObject;

public class CollectionRoadDetailParser extends MasterParser<CollectionRoadDetail> {

    @Override
    public CollectionRoadDetail parse(JSONObject data) throws Exception {
        if (data != null) {
            String customerCount = getString(data, "customerCount");
            String packCount = getString(data, "packCount");
            String turnoverBoxNum = getString(data, "turnoverBoxNum");


            CollectionRoadDetail detail = new CollectionRoadDetail();

            detail.setCustomerCount(customerCount);
            detail.setPackCount(packCount);
            detail.setTurnoverBoxNum(turnoverBoxNum);

            return detail;
        }
        return null;
    }
}
