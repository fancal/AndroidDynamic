package com.elianshang.wms.app.shelve.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.shelve.bean.ResponseState;

import org.json.JSONObject;

public class ResponseStateParser extends MasterParser<ResponseState> {

    @Override
    public ResponseState parse(JSONObject data) throws Exception {
        if (has(data, "response")) {
            return new ResponseState();
        }
        return null;
    }
}
