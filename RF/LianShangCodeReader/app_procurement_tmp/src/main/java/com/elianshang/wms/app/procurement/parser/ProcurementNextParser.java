package com.elianshang.wms.app.procurement.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.ProcurementNext;

import org.json.JSONObject;

public class ProcurementNextParser extends MasterParser<ProcurementNext> {

    @Override
    public ProcurementNext parse(JSONObject data) throws Exception {
        ProcurementNext procurementNext = null;

        if (data != null) {
            procurementNext = new ProcurementNext();

            if (has(data, "response")) {
                procurementNext.setResponseState(new ResponseStateParser().parse(data));
            } else {
                procurementNext.setProcurement(new ProcurementParser().parse(data));
            }
        }

        return procurementNext;
    }
}
