package com.elianshang.wms.app.receipt.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.receipt.bean.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderListParser extends MasterParser<OrderList> {

    @Override
    public OrderList parse(JSONObject data) throws Exception {
        OrderList orderList = null;

        if (data != null) {

            JSONArray array = optJSONArray(data, "orderOtherIdList");
            int len = getLength(array);

            if (len > 0) {
                orderList = new OrderList();
                for (int i = 0; i < len; i++) {
                    orderList.add(optString(array, i));
                }
            }
        }

        return orderList;
    }
}