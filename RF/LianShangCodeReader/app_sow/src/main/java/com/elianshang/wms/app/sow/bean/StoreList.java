package com.elianshang.wms.app.sow.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/12/7.
 */

public class StoreList implements BaseBean {

    private ArrayList<StoreList.Item> smallStoreList;

    private ArrayList<StoreList.Item> superStoreList;

    public ArrayList<Item> getSmallStoreList() {
        return smallStoreList;
    }

    public void setSmallStoreList(ArrayList<Item> smallStoreList) {
        this.smallStoreList = smallStoreList;
    }

    public ArrayList<Item> getSuperStoreList() {
        return superStoreList;
    }

    public void setSuperStoreList(ArrayList<Item> superStoreList) {
        this.superStoreList = superStoreList;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Item implements BaseBean {

        String taskId;

        String customerCode;

        String customerName;

        String storeType;

        String qty;

        String packName;

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getCustomerCode() {
            return customerCode;
        }

        public void setCustomerCode(String customerCode) {
            this.customerCode = customerCode;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}
