package com.elianshang.wms.app.transfer.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/20.
 */
public class TransferNext implements BaseBean {

    private Transfer transfer;

    private ResponseState responseState;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
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
