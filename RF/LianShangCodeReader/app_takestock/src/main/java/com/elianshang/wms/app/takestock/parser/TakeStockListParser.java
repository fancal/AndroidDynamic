package com.elianshang.wms.app.takestock.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.takestock.bean.TakeStockList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TakeStockListParser extends MasterParser<TakeStockList> {

    @Override
    public TakeStockList parse(JSONObject data) throws Exception {


        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "taskList");
            int len = getLength(jsonArray);

            if (len > 0) {
                TakeStockList takeStockList = new TakeStockList();
                for (int i = 0; i < len; i++) {
                    JSONObject jo = optJSONObject(jsonArray, i);

                    if (jo != null) {
                        String taskId = optString(jo, "taskId");
                        String locationCode = optString(jo, "locationCode");

                        if (!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(locationCode)) {
                            TakeStockList.TakeStockTask task = new TakeStockList.TakeStockTask();
                            task.setTaskId(taskId);
                            task.setLocationCode(locationCode);

                            takeStockList.add(task);
                        }
                    }
                }

                if (takeStockList.size() == 0) {
                    takeStockList = null;
                }

                return takeStockList;
            }
        }
        return null;
    }
}
