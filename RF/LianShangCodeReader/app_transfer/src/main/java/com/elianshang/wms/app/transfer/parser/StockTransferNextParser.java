package com.elianshang.wms.app.transfer.parser;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.transfer.bean.StockTransferNext;

import org.json.JSONObject;

public class StockTransferNextParser extends MasterParser<StockTransferNext> {

    @Override
    public StockTransferNext parse(JSONObject data) throws Exception {
        StockTransferNext procurementNext = null;

        if (data != null) {
            procurementNext = new StockTransferNext();

            if (has(data, "response")) {
                procurementNext.setResponseState(new ResponseStateParser().parse(data));
            } else {
                procurementNext.setStockTransfer(new StockTransferParser().parse(data));
            }
        }

        return procurementNext;
    }
}
