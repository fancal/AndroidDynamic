package com.elianshang.wms.app.shelve.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.ShelveCreate;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/3.
 */
public class ShelveCreateParser extends MasterParser<ShelveCreate> {

    @Override
    public ShelveCreate parse(JSONObject data) throws Exception {
        ShelveCreate shelve = new ShelveCreate();
        shelve.setTaskId(getString(data, "taskId"));
        return shelve;
    }
}
