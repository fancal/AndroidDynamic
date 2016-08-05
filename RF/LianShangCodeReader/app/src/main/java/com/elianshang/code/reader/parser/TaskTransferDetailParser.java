package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.TaskTransferDetail;

import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/5.
 */
public class TaskTransferDetailParser extends MasterParser<TaskTransferDetail> {
    @Override
    public TaskTransferDetail parse(JSONObject data) throws Exception {
        TaskTransferDetail bean = null;
        if (data != null) {
            bean = new TaskTransferDetail();
            bean.setFromLocationId(optString(data, "fromLocationId"));
            bean.setFromLocationName(optString(data, "fromLocationCode"));
            bean.setProductId(optString(data, "itemId"));
            bean.setProductName(optString(data, "itemName"));
            bean.setProductPackName(optString(data, "packName"));
            bean.setToLocationId(optString(data, "toLocationId"));
            bean.setToLocationName(optString(data, "toLocationCode"));
            bean.setUomQty(optString(data, "uomQty"));
        }
        return bean;
    }
}
