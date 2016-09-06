package com.elianshang.wms.rf.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.rf.bean.MenuList;
import com.elianshang.wms.rf.bean.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuListParser extends MasterParser<MenuList> {

    @Override
    public MenuList parse(JSONObject data) throws Exception {
        MenuList menuList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "menuList");
            int len = getLength(jsonArray);

            if (len > 0) {
                menuList = new MenuList();

                for (int i = 0; i < len; i++) {
                    JSONObject jo = getJSONObject(jsonArray, i);
                    if (jo != null) {
                        String url = optString(jo, "url");
                        String name = optString(jo, "name");
                        String fileName = optString(jo, "fileName");
                        String identity = optString(jo, "identity");

                        if (!TextUtils.isEmpty(url)
                                && !TextUtils.isEmpty(name)
                                && !TextUtils.isEmpty(fileName)
                                && !TextUtils.isEmpty(identity)) {
                            Menu menu = new Menu();
                            menu.setName(name);
                            menu.setUrl(url);
                            menu.setFileName(fileName);
                            menu.setIdentity(identity);

                            menuList.add(menu);
                        }
                    }

                }
            }
        }
        return menuList;
    }
}
