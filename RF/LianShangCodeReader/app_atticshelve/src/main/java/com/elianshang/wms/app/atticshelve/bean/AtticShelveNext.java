package com.elianshang.wms.app.atticshelve.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/20.
 */
public class AtticShelveNext implements BaseBean {

    private AtticShelve atticShelve;

    private ResponseState responseState;

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
