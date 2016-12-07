package com.elianshang.wms.app.sow.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.sow.bean.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/12/7.
 */

public class OrderListParser extends MasterParser<OrderList> {
    @Override
    public OrderList parse(JSONObject data) throws Exception {

        OrderList orderList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "orderList");
            int len = getLength(jsonArray);
            if (len > 0) {
                orderList = new OrderList();
                for (int i = 0; i < len; i++) {
                    String orderId = optString(jsonArray, i);
                    if (!TextUtils.isEmpty(orderId)) {
                        OrderList.Item item = new OrderList.Item();
                        item.setOrderId(orderId);
                        orderList.add(item);
                    }
                }

                if (orderList.size() == 0) {
                    orderList = null;
                }
            }

        }
        return orderList;
    }
}
