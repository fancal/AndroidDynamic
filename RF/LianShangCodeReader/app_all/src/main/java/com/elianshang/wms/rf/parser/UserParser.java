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
            long validTime = optLong(data, "validTime") * 1000;
            String userName = optString(data, "userName");
            long activeTime = System.currentTimeMillis();

            if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(utoken) && validTime > 0) {
                user = new User();
                user.setUid(uid);
                user.setUserName(userName);
                user.setValidTime(validTime);
                user.setActiveTime(activeTime);
                user.setToken(utoken);
                user.setJsonData(data.toString());
            }
        }
        return user;
    }
}
