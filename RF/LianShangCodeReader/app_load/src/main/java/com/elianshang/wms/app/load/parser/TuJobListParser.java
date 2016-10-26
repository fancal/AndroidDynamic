package com.elianshang.wms.app.load.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.load.bean.TuJobList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class TuJobListParser extends MasterParser<TuJobList> {
    @Override
    public TuJobList parse(JSONObject data) throws Exception {
        TuJobList tuJob = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "result");
            int length = getLength(jsonArray);
            if (length > 0) {
                tuJob = new TuJobList();
                for (int i = 0; i < length; i++) {
                    TuJobList.Item item = parseItem(optJSONObject(jsonArray, i));
                    if (item != null){
                        tuJob.add(item);
                    }
                }
            }
        }

        return tuJob;
    }

    private TuJobList.Item parseItem(JSONObject data) {
        TuJobList.Item item = null;
        if (data != null) {
            try {
                String containerCount = optString(data, "containerCount");
                String boxNum = optString(data, "boxNum");
                String turnoverBoxNum = optString(data, "turnoverBoxNum");
                String containerNum = optString(data, "containerNum");
                String storeNo = optString(data, "storeNo");
                String mergedTime = optString(data, "mergedTime");
                String turnoverBoxCount = optString(data, "turnoverBoxCount");
                String storeId = optString(data, "storeId");
                String packCount = optString(data, "packCount");
                String containerId = optString(data, "containerId");
                boolean isRest = optBoolean(data, "isRest");
                boolean isExpensive = optBoolean(data, "isExpensive");
                boolean isLoaded = optBoolean(data, "isLoaded");

                if (!TextUtils.isEmpty(containerId)) {
                    item = new TuJobList.Item();
                    item.setContainerCount(containerCount);
                    item.setBoxNum(boxNum);
                    item.setTurnoverBoxNum(turnoverBoxNum);
                    item.setContainerNum(containerNum);
                    item.setStoreNo(storeNo);
                    item.setMergedTime(mergedTime);
                    item.setTurnoverBoxCount(turnoverBoxCount);
                    item.setStoreId(storeId);
                    item.setPackCount(packCount);
                    item.setContainerId(containerId);
                    item.setRest(isRest);
                    item.setExpensive(isExpensive);
                    item.setLoaded(isLoaded);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return item;
    }
//
//    @Override
//    public TuJobList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
//        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161026194914\"},\"body\":{\"tuJobs\":[{\"containerCount\":3,\"isRest\":true,\"boxNum\":6,\"turnoverBoxNum\":0,\"containerNum\":3,\"storeNo\":\"6486\",\"isExpensive\":false,\"mergedTime\":1477384338,\"turnoverBoxCount\":0,\"storeId\":1,\"packCount\":6,\"containerId\":238873196130317,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":2,\"turnoverBoxNum\":0,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":1477385715,\"turnoverBoxCount\":0,\"storeId\":7,\"packCount\":2,\"containerId\":55568286898769,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":201600000088,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":201600000087,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":201600000050,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":1,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":1,\"containerId\":152329605116257,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":1,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":1,\"containerId\":152329605116256,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":180070798871541,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6486\",\"isExpensive\":false,\"mergedTime\":1477380189,\"turnoverBoxCount\":1,\"storeId\":1,\"packCount\":0,\"containerId\":10033,\"isLoaded\":true},{\"containerCount\":2,\"isRest\":true,\"boxNum\":3,\"turnoverBoxNum\":3,\"containerNum\":2,\"storeNo\":\"6486\",\"isExpensive\":false,\"mergedTime\":1477365173,\"turnoverBoxCount\":3,\"storeId\":1,\"packCount\":3,\"containerId\":108482283966616,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":4,\"turnoverBoxNum\":2,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":2,\"storeId\":7,\"packCount\":4,\"containerId\":152329605116280,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":201600000044,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6486\",\"isExpensive\":false,\"mergedTime\":1477376578,\"turnoverBoxCount\":1,\"storeId\":1,\"packCount\":0,\"containerId\":10022,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":0,\"containerId\":44014824864002,\"isLoaded\":true},{\"containerCount\":2,\"isRest\":true,\"boxNum\":4,\"turnoverBoxNum\":2,\"containerNum\":2,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":1477298769,\"turnoverBoxCount\":2,\"storeId\":7,\"packCount\":4,\"containerId\":81363860462917,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":5,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6485\",\"isExpensive\":false,\"mergedTime\":0,\"turnoverBoxCount\":1,\"storeId\":7,\"packCount\":5,\"containerId\":152329605116279,\"isLoaded\":true},{\"containerCount\":1,\"isRest\":true,\"boxNum\":0,\"turnoverBoxNum\":1,\"containerNum\":1,\"storeNo\":\"6486\",\"isExpensive\":false,\"mergedTime\":1477376605,\"turnoverBoxCount\":1,\"storeId\":1,\"packCount\":0,\"containerId\":10021,\"isLoaded\":true}]}}";
//        return super.initialParse(data);
//    }
}
