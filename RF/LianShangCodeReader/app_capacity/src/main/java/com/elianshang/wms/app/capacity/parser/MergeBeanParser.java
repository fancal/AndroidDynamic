package com.elianshang.wms.app.capacity.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.capacity.bean.MergeBean;
import com.elianshang.wms.app.capacity.bean.ResponseState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MergeBeanParser extends MasterParser<MergeBean> {

    @Override
    public MergeBean parse(JSONObject data) throws Exception {
        MergeBean mergeBean = null;
        if (data != null) {
            mergeBean = new MergeBean();

            ResponseState responseState = new ResponseStateParser().parse(data);
            mergeBean.setResponseState(responseState);

            if (responseState == null) {
                String msg = getString(data, "msg");
                mergeBean.setMsg(msg);
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

                    mergeBean.setErrCodes(codes);
                }
            }
        }

        return mergeBean;
    }
}
