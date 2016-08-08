package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.ShipScan;

import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/8.
 */
public class ShipScanParser extends MasterParser<ShipScan> {
    @Override
    public ShipScan parse(JSONObject data) throws Exception {
        ShipScan bean = null;
        if (data != null) {
            bean = new ShipScan();
            bean.setDockName(optString(data, "dockName"));
        }
        return bean;
    }
}
