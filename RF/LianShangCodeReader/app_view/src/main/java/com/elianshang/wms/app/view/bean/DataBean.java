package com.elianshang.wms.app.view.bean;

import com.xue.http.hook.BaseBean;

public class DataBean implements BaseBean {

    private String data ;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
