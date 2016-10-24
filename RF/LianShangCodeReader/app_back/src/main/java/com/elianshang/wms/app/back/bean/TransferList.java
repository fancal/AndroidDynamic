package com.elianshang.wms.app.back.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class TransferList extends ArrayList<TransferList.Item> implements BaseBean {

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Item {

        String skuName;

        String packName;

        String qty;

        String skuId;

        String realQty;

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getRealQty() {
            return realQty;
        }

        public void setRealQty(String realQty) {
            this.realQty = realQty;
        }
    }
}
