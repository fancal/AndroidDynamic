package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

public class PickLocation implements BaseBean {

    Pick pick;

    boolean done;

    boolean pickDone;

    public Pick getPick() {
        return pick;
    }

    public void setPick(Pick pick) {
        this.pick = pick;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isPickDone() {
        return pickDone;
    }

    public void setPickDone(boolean pickDone) {
        this.pickDone = pickDone;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
