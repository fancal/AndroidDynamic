package com.elianshang.wms.app.shelve.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/12/7.
 */

public class NextLocation implements BaseBean {

    private String nextLocationCode;

    private String nextLocationId;

    public String getNextLocationCode() {
        return nextLocationCode;
    }

    public void setNextLocationCode(String nextLocationCode) {
        this.nextLocationCode = nextLocationCode;
    }

    public String getNextLocationId() {
        return nextLocationId;
    }

    public void setNextLocationId(String nextLocationId) {
        this.nextLocationId = nextLocationId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
