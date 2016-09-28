package com.elianshang.wms.app.sow.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/20.
 */
public class SowNext implements BaseBean {

    private Sow sow;

    private ResponseState responseState;

    public Sow getSow() {
        return sow;
    }

    public void setSow(Sow sow) {
        this.sow = sow;
    }

    public ResponseState getResponseState() {
        return responseState;
    }

    public void setResponseState(ResponseState responseState) {
        this.responseState = responseState;
    }

    public boolean isDone() {
        if (responseState != null) {
            return true;

        }

        return false;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
