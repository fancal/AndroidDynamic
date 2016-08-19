package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class PickLocation implements BaseBean {

    Pick pick;

    boolean done;

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

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
