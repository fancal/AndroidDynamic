package com.elianshang.wms.app.qc.bean;

import com.xue.http.hook.BaseBean;

public class QCDoneState implements BaseBean {

    private boolean done;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public boolean isDone() {
        return done;
    }

    public QCDoneState setDone(boolean done) {
        this.done = done;
        return this;
    }
}
