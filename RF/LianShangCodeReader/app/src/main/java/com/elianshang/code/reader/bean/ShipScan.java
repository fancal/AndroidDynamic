package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/8.
 */
public class ShipScan implements BaseBean {

    /**
     * 码头
     */
    String dockName;

    public String getDockName() {
        return dockName;
    }

    public void setDockName(String dockName) {
        this.dockName = dockName;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
