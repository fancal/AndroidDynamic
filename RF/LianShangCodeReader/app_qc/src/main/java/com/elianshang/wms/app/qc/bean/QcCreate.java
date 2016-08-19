package com.elianshang.wms.app.qc.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/8.
 */
public class QcCreate implements BaseBean {

    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
