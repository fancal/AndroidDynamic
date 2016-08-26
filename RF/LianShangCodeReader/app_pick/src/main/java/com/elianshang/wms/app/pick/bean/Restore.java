package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

public class Restore implements BaseBean {

    private PickLocation pickLocation;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public PickLocation getPickLocation() {
        return pickLocation;
    }

    public void setPickLocation(PickLocation pickLocation) {
        this.pickLocation = pickLocation;
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
