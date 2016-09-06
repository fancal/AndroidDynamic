package com.elianshang.wms.app.receipt.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.ResponseState;

import org.json.JSONObject;

public class ResponseStateParser extends MasterParser<ResponseState> {

    @Override
    public ResponseState parse(JSONObject data) throws Exception {
        ResponseState responseState = null;
        if (data != null) {
            boolean state = optBoolean(data, "response");
            if (state) {
                responseState = new ResponseState();
            }
        }
        return responseState;
    }
}