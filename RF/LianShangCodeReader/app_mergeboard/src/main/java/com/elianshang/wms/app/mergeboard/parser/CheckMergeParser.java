package com.elianshang.wms.app.mergeboard.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.mergeboard.bean.CheckMerge;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckMergeParser extends MasterParser<CheckMerge> {

    @Override
    public CheckMerge parse(JSONObject data) throws Exception {

        if (data != null) {
            String containerCount = optString(data, "containerCount");
            String turnoverBoxCount = optString(data, "turnoverBoxCount");
            String customerName = optString(data, "customerName");
            String customerCode = optString(data, "customerCode");
            String packCount = optString(data, "packCount");
            String taskBoardQty = optString(data, "taskBoardQty");

            if (!TextUtils.isEmpty(containerCount)
                    && !TextUtils.isEmpty(turnoverBoxCount)
                    && !TextUtils.isEmpty(packCount)) {

                JSONArray array = optJSONArray(data, "details");
                int len = getLength(array);

                if (len > 0) {
                    CheckMerge checkMerge = new CheckMerge();

                    checkMerge.setContainerCount(containerCount);
                    checkMerge.setTurnoverBoxCount(turnoverBoxCount);
                    checkMerge.setCustomerCode(customerCode);
                    checkMerge.setCustomerName(customerName);
                    checkMerge.setPackCount(packCount);
                    checkMerge.setTaskBoardQty(taskBoardQty);

                    for (int i = 0; i < len; i++) {
                        JSONObject object = optJSONObject(array, i);
                        boolean iisMerged = optBoolean(object, "isMerged");
                        String iturnoverBoxCount = optString(object, "turnoverBoxCount");
                        String ipackCount = optString(object, "packCount");
                        String icontainerId = optString(object, "containerId");

                        CheckMerge.Item item = new CheckMerge.Item();
                        item.setMerged(iisMerged);
                        item.setTurnoverBoxCount(iturnoverBoxCount);
                        item.setPackCount(ipackCount);
                        item.setContainerId(icontainerId);

                        checkMerge.add(item);
                    }

                    return checkMerge;
                }
            }
        }
        return null;
    }
}
