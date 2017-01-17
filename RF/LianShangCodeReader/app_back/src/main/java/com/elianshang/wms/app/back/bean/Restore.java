package com.elianshang.wms.app.back.bean;

import com.xue.http.hook.BaseBean;

public class Restore implements BaseBean {

    private BackList backList;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public BackList getBackList() {
        return backList;
    }

    public void setBackList(BackList backList) {
        this.backList = backList;
    }

    public ResponseState getResponseState() {
        return responseState;
    }

    public void setResponseState(ResponseState responseState) {
        this.responseState = responseState;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
