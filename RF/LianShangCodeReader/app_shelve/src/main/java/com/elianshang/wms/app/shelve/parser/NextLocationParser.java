package com.elianshang.wms.app.shelve.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.NextLocation;

import org.json.JSONObject;

public class NextLocationParser extends MasterParser<NextLocation> {

    @Override
    public NextLocation parse(JSONObject data) throws Exception {

        NextLocation nextLocation = new NextLocation();
        nextLocation.setNextLocationCode(optString(data, "nextLocationCode"));
        nextLocation.setNextLocationId(optString(data, "nextLocationId"));
        return nextLocation;
    }
}
