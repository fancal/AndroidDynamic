package com.elianshang.wms.app.mergeboard.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class CheckMerge extends ArrayList<CheckMerge.Item> implements BaseBean {

    private String containerCount;

    private String deliveryCode;

    private String turnoverBoxCount;

    private String deliveryName;

    private String packCount;

    private String taskBoardQty;

    private String storeName;

    private String storeNo;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(String containerCount) {
        this.containerCount = containerCount;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getTurnoverBoxCount() {
        return turnoverBoxCount;
    }

    public void setTurnoverBoxCount(String turnoverBoxCount) {
        this.turnoverBoxCount = turnoverBoxCount;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getPackCount() {
        return packCount;
    }

    public void setPackCount(String packCount) {
        this.packCount = packCount;
    }

    public String getTaskBoardQty() {
        return taskBoardQty;
    }

    public void setTaskBoardQty(String taskBoardQty) {
        this.taskBoardQty = taskBoardQty;
    }

    public static class Item {

        private boolean isMerged;

        private String turnoverBoxCount;

        private String packCount;

        private String containerId;

        public boolean isMerged() {
            return isMerged;
        }

        public void setMerged(boolean merged) {
            isMerged = merged;
        }

        public String getTurnoverBoxCount() {
            return turnoverBoxCount;
        }

        public void setTurnoverBoxCount(String turnoverBoxCount) {
            this.turnoverBoxCount = turnoverBoxCount;
        }

        public String getPackCount() {
            return packCount;
        }

        public void setPackCount(String packCount) {
            this.packCount = packCount;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
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
