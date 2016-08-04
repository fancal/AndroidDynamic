package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.ShelveCreate;

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
