package com.elianshang.wms.rf.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/8/24.
 */
public class MenuList extends ArrayList<Menu> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
