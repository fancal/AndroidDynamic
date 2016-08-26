package com.elianshang.wms.app.atticshelve.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.atticshelve.bean.Restore;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/24.
 */
public class RestoreParser extends MasterParser<Restore> {
    @Override
    public Restore parse(JSONObject data) throws Exception {
        Restore restore = null;
        if (data != null) {
            restore = new Restore();
            if (has(data, "response")) {
                restore.setResponseState(new ResponseStateParser().parse(data));
            } else {
                restore.setAtticShelve(new AtticShelveParser().parse(data));
            }
        }
        return restore;
    }
}
