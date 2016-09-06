package com.elianshang.wms.rf.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.rf.bean.User;

import org.json.JSONObject;

public class UserParser extends MasterParser<User> {

    @Override
    public User parse(JSONObject data) throws Exception {
        User user = null;
        if (data != null) {
            String uid = optString(data, "uid");
            String utoken = optString(data, "utoken");

            if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(utoken)) {
                user = new User();
                user.setUid(uid);
                user.setToken(utoken);
                user.setJsonData(data.toString());
            }
        }
        return user;
    }
}
