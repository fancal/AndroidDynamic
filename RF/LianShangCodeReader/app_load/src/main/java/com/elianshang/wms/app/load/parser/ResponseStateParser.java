package com.elianshang.wms.app.load.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.load.bean.ResponseState;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;

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

    @Override
    public ResponseState initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\n" +
                "\"head\": {\n" +
                "\"status\": 1,\n" +
                "\"message\": \"success.\",\n" +
                "\"timestamp\": \"20161024161404\"\n" +
                "},\n" +
                "\"body\": {\n" +
                "\"response\": true\n" +
                "}\n" +
                "}";
        return super.initialParse(data);
    }

}
