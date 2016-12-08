package com.elianshang.wms.app.procurement.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.ZoneList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class ZoneListParser extends MasterParser<ZoneList> {

    @Override
    public ZoneList parse(JSONObject data) throws Exception {
        ZoneList zoneList = null;

        if (data != null) {

            JSONArray array = optJSONArray(data, "zone_list");
            int len = getLength(array);

            if (len > 0) {
                zoneList = new ZoneList();

                for (int i = 0; i < len; i++) {
                    JSONObject object = optJSONObject(array, i);

                    if (object != null) {
                        ZoneList.Zone zone = new ZoneList.Zone();

                        zone.setZoneName(optString(object, "zoneName"));
                        zone.setWorkerNum(optString(object, "workerNum"));
                        zone.setTaskNum(optString(object, "taskNum"));
                        zone.setZoneId(optString(object, "zoneId"));

                        zoneList.add(zone);
                    }
                }
            }
        }

        return zoneList;
    }
}
