package com.elianshang.wms.app.sow.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.sow.bean.StoreList;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/12/7.
 */

public class StoreListParser extends MasterParser<StoreList> {
    @Override
    public StoreList parse(JSONObject data) throws Exception {

        StoreList storeList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "storeInfo");
            int len = getLength(jsonArray);
            if (len > 0) {
                storeList = new StoreList();
                ArrayList<StoreList.Item> smallStoreList = new ArrayList<>();
                ArrayList<StoreList.Item> superStoreList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    StoreList.Item item = parseItem(jsonArray.optJSONObject(i));
                    if (item != null) {
                        if (TextUtils.equals("store", item.getStoreType())) {
                            smallStoreList.add(item);
                        } else {
                            superStoreList.add(item);
                        }
                    }
                }
                if (smallStoreList.size() > 0) {
                    storeList.setSmallStoreList(smallStoreList);
                }
                if (superStoreList.size() > 0) {
                    storeList.setSuperStoreList(superStoreList);
                }

                if (superStoreList.size() == 0 && superStoreList.size() == 0) {
                    storeList = null;
                }

            }

        }
        return storeList;
    }

    private StoreList.Item parseItem(JSONObject data) throws JSONException {
        StoreList.Item item = null;

        if (data != null) {
            String customerCode = optString(data, "customerCode");
            String taskId = optString(data, "taskId");
            String customerName = optString(data, "customerName");
            String storeType = optString(data, "storeType");
            String qty = optString(data,"qty");
            String packName = optString(data,"packName");

            if (!TextUtils.isEmpty(customerCode) && !TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(customerName) && !TextUtils.isEmpty(storeType)) {
                item = new StoreList.Item();
                item.setCustomerCode(customerCode);
                item.setTaskId(taskId);
                item.setCustomerName(customerName);
                item.setStoreType(storeType);
                item.setQty(qty);
                item.setPackName(packName);
            }
        }
        return item;
    }

    @Override
    public StoreList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161208115146\"},\"body\":{\"storeInfo\":[{\"taskId\":1816120800000014,\"customerName\":\"惠新店\",\"customerCode\":\"1163\",\"storeType\":\"super_market\",\"qty\":1,\"packName\":\"H12\"}]}}";
        return super.initialParse(data);
    }
}
