package com.elianshang.wms.app.takestock.bean;

import com.xue.http.hook.BaseBean;

public class Restore implements BaseBean {

    private TakeStockList takeStockList;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public TakeStockList getTakeStockList() {
        return takeStockList;
    }

    public void setTakeStockList(TakeStockList takeStockList) {
        this.takeStockList = takeStockList;
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
