package com.elianshang.wms.app.qc.bean;

import com.xue.http.hook.BaseBean;

public class ConfirmResponseState implements BaseBean {

    private boolean confirmState;

    private boolean loadState;

    public boolean isConfirmState() {
        return confirmState;
    }

    public void setConfirmState(boolean confirmState) {
        this.confirmState = confirmState;
    }

    public boolean isLoadState() {
        return loadState;
    }

    public void setLoadState(boolean loadState) {
        this.loadState = loadState;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

}
