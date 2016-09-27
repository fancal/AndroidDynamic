package com.elianshang.wms.app.sow.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.sow.bean.SowNext;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class SowNextParser extends MasterParser<SowNext> {

    @Override
    public SowNext parse(JSONObject data) throws Exception {
        SowNext sowNext = null;
        if (data != null) {
            sowNext = new SowNext();
            if (has(data, "response")) {
                sowNext.setResponseState(new ResponseStateParser().parse(data));
            } else {
                sowNext.setSow(new SowParser().parse(data));
            }
        }
        return sowNext;
    }
}
