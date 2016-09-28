package com.elianshang.wms.app.mergeboard.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.mergeboard.bean.BoardDetailList;

import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/4.
 */
public class BoardDetailListParser extends MasterParser<BoardDetailList> {

    @Override
    public BoardDetailList parse(JSONObject data) throws Exception {

        if (data != null) {
            String viewType = optString(data, "viewType");
            String taskId = optString(data, "taskId");
            String locationCode = optString(data, "locationCode");
            String itemName = optString(data, "itemName");
            String packName = optString(data, "packName");
            String qty = optString(data, "qty");

            if (!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(locationCode)) {
                BoardDetailList detail = new BoardDetailList();

                return detail;
            }
        }
        return null;
    }
}
