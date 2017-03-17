package com.elianshang.wms.app.qc.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.qc.bean.ConfirmResponseState;

import org.json.JSONObject;

public class ConfirmResponseStateParser extends MasterParser<ConfirmResponseState> {

    @Override
    public ConfirmResponseState parse(JSONObject data) throws Exception {
        if (data != null) {
            boolean state = getBoolean(data, "response");
            boolean isCanLoad = getBoolean(data, "isCanLoad");
            if (state) {
                ConfirmResponseState confirmResponseState = new ConfirmResponseState();
                confirmResponseState.setConfirmState(state);
                confirmResponseState.setLoadState(isCanLoad);

                return confirmResponseState;
            }
        }
        return null;
    }
}
