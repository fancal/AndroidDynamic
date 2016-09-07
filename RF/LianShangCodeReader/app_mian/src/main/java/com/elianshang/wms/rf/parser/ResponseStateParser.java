package com.elianshang.wms.rf.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.rf.bean.ResponseState;

import org.json.JSONObject;

public class ResponseStateParser extends MasterParser<ResponseState> {

    @Override
    public ResponseState parse(JSONObject data) throws Exception {
        if (data != null) {
            if (has(data, "response")) {
                return new ResponseState();
            }
        }
        return null;
    }
}
