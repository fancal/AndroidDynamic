package com.elianshang.wms.app.transfer.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.transfer.bean.TransferNext;

import org.json.JSONObject;

public class StockTransferNextParser extends MasterParser<TransferNext> {

    @Override
    public TransferNext parse(JSONObject data) throws Exception {
        TransferNext procurementNext = null;

        if (data != null) {
            procurementNext = new TransferNext();

            if (has(data, "response")) {
                procurementNext.setResponseState(new ResponseStateParser().parse(data));
            } else {
                procurementNext.setTransfer(new StockTransferParser().parse(data));
            }
        }

        return procurementNext;
    }
}
