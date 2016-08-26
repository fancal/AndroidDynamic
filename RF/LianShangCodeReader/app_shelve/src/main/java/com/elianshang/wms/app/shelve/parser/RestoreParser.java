package com.elianshang.wms.app.shelve.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.Restore;

import org.json.JSONObject;

public class RestoreParser extends MasterParser<Restore> {
    @Override
    public Restore parse(JSONObject data) throws Exception {
        Restore restore = null;
        if (data != null) {
            restore = new Restore();
            if (has(data, "response")) {
                restore.setResponseState(new ResponseStateParser().parse(data));
            } else {
                restore.setShelve(new ShelveParser().parse(data));
            }
        }
        return restore;
    }
}
