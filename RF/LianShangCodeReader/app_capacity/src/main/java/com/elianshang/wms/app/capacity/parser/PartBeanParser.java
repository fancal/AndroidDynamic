package com.elianshang.wms.app.capacity.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.capacity.bean.PartBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PartBeanParser extends MasterParser<PartBean> {

    @Override
    public PartBean parse(JSONObject data) throws Exception {
        PartBean partBean = null;
        if (data != null) {
            partBean = new PartBean();

            boolean canSplit = getBoolean(data, "canSplit");
            partBean.setCanSplit(canSplit);

            if (canSplit) {
                JSONArray jsonArray = getJSONArray(data, "arr");
                int len = jsonArray.length();

                if (len > 0) {
                    ArrayList<String> codes = new ArrayList();
                    for (int i = 0; i < len; i++) {
                        String locationCode = getString(jsonArray, i);

                        if (!TextUtils.isEmpty(locationCode)) {
                            codes.add(locationCode);
                        }
                    }

                    partBean.setCodes(codes);
                }
            } else {
                String msg = getString(data, "msg");
                partBean.setMsg(msg);
            }
        }

        return partBean;
    }
}
