package com.elianshang.wms.app.qc.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.qc.bean.QCDoneState;

import org.json.JSONObject;

public class QCDoneParser extends MasterParser<QCDoneState> {

    @Override
    public QCDoneState parse(JSONObject data) throws Exception {
        if (data != null) {
            boolean state = optBoolean(data, "qcDone");
            return new QCDoneState().setDone(state);
        }
        return null;
    }
}
