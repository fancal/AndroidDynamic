package com.elianshang.wms.app.load.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.load.bean.ExpensiveList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class ExpensiveListParser extends MasterParser<ExpensiveList> {
    @Override
    public ExpensiveList parse(JSONObject data) throws Exception {
        ExpensiveList tuJob = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "result");
            int length = getLength(jsonArray);
            if (length > 0) {
                tuJob = new ExpensiveList();
                for (int i = 0; i < length; i++) {
                    ExpensiveList.Item item = parseItem(optJSONObject(jsonArray, i));
                    if (item != null) {
                        tuJob.add(item);
                    }
                }

                if (tuJob.size() == 0) {
                    tuJob = null;
                }
            }
        }

        return tuJob;
    }

    private ExpensiveList.Item parseItem(JSONObject data) {
        ExpensiveList.Item item = null;
        if (data != null) {
            try {
                String containerCount = optString(data, "containerCount");
                String boxNum = optString(data, "boxNum");
                String turnoverBoxNum = optString(data, "turnoverBoxNum");
                String containerNum = optString(data, "containerNum");
                String storeNo = optString(data, "storeNo");
                String turnoverBoxCount = optString(data, "turnoverBoxCount");
                String storeId = optString(data, "storeId");
                String packCount = optString(data, "packCount");
                String containerId = optString(data, "containerId");
                String markContainerId = optString(data, "markContainerId");
                boolean isRest = optBoolean(data, "isRest");
                boolean isExpensive = optBoolean(data, "isExpensive");
                boolean isLoaded = optBoolean(data, "isLoaded");
                String locationCode = optString(data, "locationCode");

                if (!TextUtils.isEmpty(containerId)) {
                    item = new ExpensiveList.Item();
                    item.setContainerCount(containerCount);
                    item.setBoxNum(boxNum);
                    item.setTurnoverBoxNum(turnoverBoxNum);
                    item.setContainerNum(containerNum);
                    item.setStoreNo(storeNo);
                    item.setTurnoverBoxCount(turnoverBoxCount);
                    item.setStoreId(storeId);
                    item.setPackCount(packCount);
                    item.setContainerId(containerId);
                    item.setMarkContainerId(markContainerId);
                    item.setRest(isRest);
                    item.setExpensive(isExpensive);
                    item.setLoaded(isLoaded);
                    item.setLocationCode(locationCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return item;
    }

//    @Override
//    public ExpensiveList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
//        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161125112304\"},\"body\":{\"result\":[{\"containerCount\":1,\"isRest\":false,\"boxNum\":1.0000,\"turnoverBoxNum\":1,\"taskBoardQty\":0,\"storeNo\":\"1546\",\"locationCode\":\"DC10-G1-R3\",\"isExpensive\":true,\"mergedTime\":0,\"turnoverBoxCount\":1,\"markContainerId\":45651207398550,\"containerId\":45651207398550,\"storeId\":12,\"packCount\":1.0000,\"isLoaded\":false},{\"containerCount\":1,\"isRest\":false,\"boxNum\":1.0000,\"turnoverBoxNum\":1,\"taskBoardQty\":0,\"storeNo\":\"1546\",\"locationCode\":\"DC10-G1-R3\",\"isExpensive\":true,\"mergedTime\":0,\"turnoverBoxCount\":1,\"markContainerId\":45651207398550,\"containerId\":45651207398550,\"storeId\":12,\"packCount\":1.0000,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":false,\"boxNum\":1.0000,\"turnoverBoxNum\":1,\"taskBoardQty\":0,\"storeNo\":\"1546\",\"locationCode\":\"DC10-G1-R3\",\"isExpensive\":true,\"mergedTime\":0,\"turnoverBoxCount\":1,\"markContainerId\":45651207398550,\"containerId\":45651207398550,\"storeId\":12,\"packCount\":1.0000,\"isLoaded\":false}]}}";
//        return super.initialParse(data);
//    }
}
