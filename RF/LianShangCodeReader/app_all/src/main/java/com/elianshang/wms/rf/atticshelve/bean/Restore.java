package com.elianshang.wms.rf.atticshelve.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/24.
 */
public class Restore implements BaseBean {

    private AtticShelve atticShelve;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public AtticShelve getAtticShelve() {
        return atticShelve;
    }

    public void setAtticShelve(AtticShelve atticShelve) {
        this.atticShelve = atticShelve;
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
