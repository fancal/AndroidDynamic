package com.elianshang.wms.app.procurement.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.LocationList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class LocationListParser extends MasterParser<LocationList> {

    @Override
    public LocationList parse(JSONObject data) throws Exception {
        LocationList locationList = null;

        if (data != null) {

            JSONArray array = optJSONArray(data, "task_list");
            int len = getLength(array);

            if (len > 0) {
                locationList = new LocationList();

                for (int i = 0; i < len; i++) {
                    JSONObject object = optJSONObject(array, i);

                    if (object != null) {
                        LocationList.Location location = new LocationList.Location();

                        location.setTaskId(optString(object, "taskId"));
                        location.setLocationCode(optString(object, "locationCode"));
                        location.setPriority(optString(object, "priority"));
                        location.setPackCount(optString(object, "packCount"));

                        locationList.add(location);
                    }
                }
            }
        }

        return locationList;
    }
}
