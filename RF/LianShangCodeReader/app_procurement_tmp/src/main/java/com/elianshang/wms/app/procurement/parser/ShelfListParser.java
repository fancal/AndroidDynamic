package com.elianshang.wms.app.procurement.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.ShelfList;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/12/5.
 */

public class ShelfListParser extends MasterParser<ShelfList> {
    @Override
    public ShelfList parse(JSONObject data) throws Exception {
        ShelfList shelfList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "list");
            int length = getLength(jsonArray);
            if (length > 0) {
                shelfList = new ShelfList();
                for (int i = 0; i < length; i++) {
                    ShelfList.Item item = getItem(optJSONObject(jsonArray, i));
                    if (item != null) {
                        shelfList.add(item);
                    }
                }
                if (shelfList.size() == 0) {
                    shelfList = null;
                }
            }
        }
        return shelfList;
    }

    private ShelfList.Item getItem(JSONObject data) throws JSONException {
        ShelfList.Item item = null;
        if (data != null) {
            String name = optString(data, "name");
            String id = optString(data, "id");
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
                item = new ShelfList.Item();
                item.setName(name);
                item.setId(id);

            }
        }
        return item;
    }

    @Override
    public ShelfList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161205180350\"},\"body\":{\"list\":[{\"id\":\"111\",\"name\":\"货架区1\"},{\"id\":\"222\",\"name\":\"货架区2\"},{\"id\":\"333\",\"name\":\"货架区3\"},{\"id\":\"444\",\"name\":\"货架区4\"},{\"id\":\"555\",\"name\":\"货架区5\"}]}}";
        return super.initialParse(data);
    }
}
