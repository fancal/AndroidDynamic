package com.elianshang.wms.app.view.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.view.bean.DataBean;

import org.json.JSONObject;

/**
 * Created by xfilshy on 17/1/8.
 */

public class DataBeanParser extends MasterParser<DataBean> {

    @Override
    public DataBean parse(JSONObject data) throws Exception {
        DataBean dataBean = null;
        String text = optString(data, "data");

        if (!TextUtils.isEmpty(text)) {
            dataBean = new DataBean();
            dataBean.setData(text);
        }

        return dataBean;
    }
}
