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

        String storeNo;

        String taskId;

        String storeName;

        String storeType;

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getStoreNo() {
            return storeNo;
        }

        public void setStoreNo(String storeNo) {
            this.storeNo = storeNo;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
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
