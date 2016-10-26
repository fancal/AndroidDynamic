package com.elianshang.wms.app.load.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class ContainerInfo implements BaseBean{

    boolean isRest;

    String boxNum;

    String turnoverBoxNum;

    String containerNum;

    boolean isExpensive;

    String storeId;

    String containerId;

    boolean isLoaded;

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean rest) {
        isRest = rest;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public String getTurnoverBoxNum() {
        return turnoverBoxNum;
    }

    public void setTurnoverBoxNum(String turnoverBoxNum) {
        this.turnoverBoxNum = turnoverBoxNum;
    }

    public String getContainerNum() {
        return containerNum;
    }

    public void setContainerNum(String containerNum) {
        this.containerNum = containerNum;
    }

    public boolean isExpensive() {
        return isExpensive;
    }

    public void setExpensive(boolean expensive) {
        isExpensive = expensive;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
