package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.ResponseState;

import org.json.JSONObject;

public class ResponseStateParser extends MasterParser<ResponseState> {

    @Override
    public ResponseState parse(JSONObject data) throws Exception {
        if (data != null) {
            return new ResponseState();
        }
        return null;
    }
}
