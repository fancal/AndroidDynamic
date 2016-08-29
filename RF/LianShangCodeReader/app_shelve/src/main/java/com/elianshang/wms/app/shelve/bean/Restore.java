package com.elianshang.wms.app.shelve.bean;

import com.xue.http.hook.BaseBean;

public class Restore implements BaseBean {

    private Shelve shelve;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public Shelve getShelve() {
        return shelve;
    }

    public void setShelve(Shelve shelve) {
        this.shelve = shelve;
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
