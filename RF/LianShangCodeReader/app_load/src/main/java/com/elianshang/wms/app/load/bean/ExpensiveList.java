package com.elianshang.wms.app.load.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class ExpensiveList implements BaseBean {

    private ArrayList<Item> tuJobList;

    private boolean openSwitch;

    public ArrayList<Item> getTuJobList() {
        return tuJobList;
    }

    public boolean add(Item item) {
        if (tuJobList == null) {
            tuJobList = new ArrayList<>();
        }
        return tuJobList.add(item);
    }

    public int size() {
        if (tuJobList == null) {
            return 0;
        }
        return tuJobList.size();
    }

    public boolean remove(Item item) {
        if (tuJobList == null) {
            return false;
        }
        return tuJobList.remove(item);
    }

    public Item get(int index) {
        if (tuJobList == null) {
            return null;
        }
        return tuJobList.get(index);
    }

    public boolean isOpenSwitch() {
        return openSwitch;
    }

    public void setOpenSwitch(boolean openSwitch) {
        this.openSwitch = openSwitch;
    }

    public void clear() {
        if (tuJobList == null) {
            return;
        }
        tuJobList.clear();
    }

    public static class Item implements BaseBean {

        String containerCount;

        boolean isRest;

        String boxNum;

        String turnoverBoxNum;

        String containerNum;

        String storeNo;

        boolean isExpensive;

        String turnoverBoxCount;

        String storeId;

        String packCount;

        String markContainerId;

        String containerId;

        boolean isLoaded;

        String locationCode ;

        public String getLocationCode() {
            return locationCode;
        }

        public void setLocationCode(String locationCode) {
            this.locationCode = locationCode;
        }

        public String getContainerCount() {
            return containerCount;
        }

        public void setContainerCount(String containerCount) {
            this.containerCount = containerCount;
        }

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

        public String getStoreNo() {
            return storeNo;
        }

        public void setStoreNo(String storeNo) {
            this.storeNo = storeNo;
        }

        public boolean isExpensive() {
            return isExpensive;
        }

        public void setExpensive(boolean expensive) {
            isExpensive = expensive;
        }

        public String getTurnoverBoxCount() {
            return turnoverBoxCount;
        }

        public void setTurnoverBoxCount(String turnoverBoxCount) {
            this.turnoverBoxCount = turnoverBoxCount;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getPackCount() {
            return packCount;
        }

        public void setPackCount(String packCount) {
            this.packCount = packCount;
        }

        public String getMarkContainerId() {
            return markContainerId;
        }

        public void setMarkContainerId(String markContainerId) {
            this.markContainerId = markContainerId;
        }

        public boolean isLoaded() {
            return isLoaded;
        }

        public void setLoaded(boolean loaded) {
            isLoaded = loaded;
        }

        public String getContainerNum() {
            return containerNum;
        }

        public void setContainerNum(String containerNum) {
            this.containerNum = containerNum;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
        }

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
