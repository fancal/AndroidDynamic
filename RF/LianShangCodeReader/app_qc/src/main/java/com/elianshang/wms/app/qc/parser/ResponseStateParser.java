package com.elianshang.wms.app.qc.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.qc.bean.ResponseState;

import org.json.JSONObject;

public class ResponseStateParser extends MasterParser<ResponseState> {

    @Override
    public ResponseState parse(JSONObject data) throws Exception {
        if (data != null) {
            boolean state = getBoolean(data, "response");
            if (state) {
                return new ResponseState();
            }
        }
        return null;
    }
}
