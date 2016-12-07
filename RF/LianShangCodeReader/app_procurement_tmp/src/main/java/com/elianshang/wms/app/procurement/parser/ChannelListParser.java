package com.elianshang.wms.app.procurement.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.procurement.bean.ChannelList;
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

public class ChannelListParser extends MasterParser<ChannelList> {
    @Override
    public ChannelList parse(JSONObject data) throws Exception {
        ChannelList channelList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "list");
            int length = getLength(jsonArray);
            if (length > 0) {
                channelList = new ChannelList();
                for (int i = 0; i < length; i++) {
                    ChannelList.Item item = getItem(optJSONObject(jsonArray, i));
                    if (item != null) {
                        channelList.add(item);
                    }
                }
                if (channelList.size() == 0) {
                    channelList = null;
                }
            }
        }
        return channelList;
    }

    private ChannelList.Item getItem(JSONObject data) throws JSONException {
        ChannelList.Item item = null;
        if (data != null) {
            String name = optString(data, "name");
            String id = optString(data, "id");
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
                item = new ChannelList.Item();
                item.setName(name);
                item.setId(id);

            }
        }
        return item;
    }

    @Override
    public ChannelList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161205180350\"},\"body\":{\"list\":[{\"id\":\"111\",\"name\":\"通道1\"},{\"id\":\"222\",\"name\":\"通道区2\"},{\"id\":\"333\",\"name\":\"通道区3\"},{\"id\":\"444\",\"name\":\"通道区4\"}]}}";
        return super.initialParse(data);
    }
}
