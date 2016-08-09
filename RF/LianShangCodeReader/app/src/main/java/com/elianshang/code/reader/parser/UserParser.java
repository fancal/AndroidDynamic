package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.User;

import org.json.JSONObject;

public class UserParser extends MasterParser<User> {
    @Override
    public User parse(JSONObject data) throws Exception {
        User user = null;
        if (data != null) {
            user = new User();

            user.setUid(getString(data, "uid"));
//            user.setUsername(getString(data, "username"));
//            user.setCellphone(getString(data, "cellphone"));
//            user.setStatus(getString(data, "status"));
            user.setToken(getString(data, "utoken"));
            user.setJsonData(data.toString());
        }
        return user;
    }
}
