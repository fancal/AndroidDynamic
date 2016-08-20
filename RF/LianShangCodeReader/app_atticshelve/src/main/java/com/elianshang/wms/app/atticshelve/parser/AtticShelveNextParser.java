package com.elianshang.wms.app.atticshelve.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.atticshelve.bean.AtticShelveNext;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/20.
 */
public class AtticShelveNextParser extends MasterParser<AtticShelveNext> {

    @Override
    public AtticShelveNext parse(JSONObject data) throws Exception {
        AtticShelveNext atticShelveNext = null;
        if (data != null) {
            atticShelveNext = new AtticShelveNext();
            if (has(data, "response")) {
                atticShelveNext.setResponseState(new ResponseStateParser().parse(data));
            } else {
                atticShelveNext.setAtticShelve(new AtticShelveParser().parse(data));
            }
        }
        return atticShelveNext;
    }
}
