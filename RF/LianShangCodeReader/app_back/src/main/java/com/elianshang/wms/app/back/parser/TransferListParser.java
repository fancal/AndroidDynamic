package com.elianshang.wms.app.back.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.back.bean.TransferList;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;

public class TransferListParser extends MasterParser<TransferList> {

    @Override
    public TransferList parse(JSONObject data) throws Exception {
        TransferList transferList = null;
        if (data != null) {
            transferList = new TransferList();
            String taskId = optString(data, "taskId");

            if (!TextUtils.isEmpty(taskId)) {
                transferList.setTaskId(taskId);
            }

            JSONArray jsonArray = optJSONArray(data, "list");
            int len = getLength(jsonArray);

            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    JSONObject jo = optJSONObject(jsonArray, i);
                    if (jo != null) {
                        String skuName = optString(jo, "skuName");
                        String packName = optString(jo, "packName");
                        String skuId = optString(jo, "skuId");
                        String qty = optString(jo, "qty");

                        if (!TextUtils.isEmpty(skuName)
                                && !TextUtils.isEmpty(packName)
                                && !TextUtils.isEmpty(skuId)
                                && !TextUtils.isEmpty(qty)) {

                            TransferList.Item item = new TransferList.Item();
                            item.setSkuName(skuName);
                            item.setPackName(packName);
                            item.setQty(qty);
                            item.setSkuId(skuId);

                            transferList.add(item);
                        }
                    }

                }

                return transferList;
            }
        }
        return null;
    }

    @Override
    public TransferList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\n" +
                "\"head\": {\n" +
                "\"status\": 1,\n" +
                "\"message\": \"success.\",\n" +
                "\"timestamp\": \"20160801142217\"\n" +
                "},\n" +
                "\"body\":\n" +
                "{\n" +
                "\"taskId\":\"21321314\",\n" +
                "\"list\":[\n" +
                "           {\n" +
                "       \"skuName\":\"呵呵哒\",\n" +
                "\"packName\":\"H12\",\n" +
                "\"qty\":12,\n" +
                "\"skuId\":\"21312312412\"\n" +
                "          },\n" +
                "         {\n" +
                "  \"skuName\":\"呵呵哒\",\n" +
                " \"packName\":\"H12\",\n" +
                " \"qty\":12,\n" +
                " \"skuId\":\"21312312412\"\n" +
                "         }\n" +
                "]\n" +
                "}\n" +
                "}";
        return super.initialParse(data);
    }

}
