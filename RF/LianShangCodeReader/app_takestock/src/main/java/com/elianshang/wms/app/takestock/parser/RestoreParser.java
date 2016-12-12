package com.elianshang.wms.app.takestock.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.takestock.bean.Restore;

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
                restore.setTakeStockList(new TakeStockListParser().parse(data));
            }
        }
        return restore;
    }
}
