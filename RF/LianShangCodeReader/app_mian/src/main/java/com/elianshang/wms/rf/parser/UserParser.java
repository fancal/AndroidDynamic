package com.elianshang.wms.rf.parser;


import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.rf.bean.User;

import org.json.JSONObject;

public class UserParser extends MasterParser<User> {

    @Override
    public User parse(JSONObject data) throws Exception {
        User user = null;
        if (data != null) {
            user = new User();

            user.setUid(getString(data, "uid"));
            user.setToken(getString(data, "utoken"));
            user.setJsonData(data.toString());
        }
        return user;
    }
}
