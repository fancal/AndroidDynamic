package com.elianshang.wms.app.load.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.load.bean.ContainerInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class ContainerInfoParser extends MasterParser<ContainerInfo> {
    @Override
    public ContainerInfo parse(JSONObject data) throws Exception {
        ContainerInfo item = null;
        if (data != null) {
            try {
                String boxNum = optString(data, "boxNum");
                String turnoverBoxNum = optString(data, "turnoverBoxNum");
                String containerNum = optString(data, "containerNum");
                String storeId = optString(data, "storeId");
                String containerId = optString(data, "containerId");
                boolean isRest = optBoolean(data, "isRest");
                boolean isExpensive = optBoolean(data, "isExpensive");
                boolean isLoaded = optBoolean(data, "isLoaded");
                int taskBoardQty = optInt(data,"taskBoardQty");

                if (!TextUtils.isEmpty(containerId)) {
                    item = new ContainerInfo();
                    item.setBoxNum(boxNum);
                    item.setTurnoverBoxNum(turnoverBoxNum);
                    item.setContainerNum(containerNum);
                    item.setStoreId(storeId);
                    item.setContainerId(containerId);
                    item.setRest(isRest);
                    item.setExpensive(isExpensive);
                    item.setLoaded(isLoaded);
                    item.setTaskBoardQty(taskBoardQty);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return item;
    }

//    @Override
//    public ContainerInfo initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
//        data = "{\n" +
//                "\"head\": {\n" +
//                "\"status\": 1,\n" +
//                "\"message\": \"success.\",\n" +
//                "\"timestamp\": \"20161025200019\"\n" +
//                "},\n" +
//                "\"body\": {\n" +
//                "\"isExpensive\":false, \n" +
//                "\"isRest\": false,\n" +
//                "\"boxNum\": 1,\n" +
//                "\"turnoverBoxNum\": 1, \n" +
//                "\"containerId\": 230034153429443, \n" +
//                "\"storeId\": 7,  \n" +
//                "\"isLoaded\": true, \n" +
//                "\"containerNum\": 1  \n" +
//                "}\n" +
//                "}";
//        return super.initialParse(data);
//    }
}
