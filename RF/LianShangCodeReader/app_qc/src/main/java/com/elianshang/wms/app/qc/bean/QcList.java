package com.elianshang.wms.app.qc.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class QcList extends ArrayList<QcList.Item> implements BaseBean {

    private String customerName;

    private String containerType;

    private String qcTaskId;

    private String customerId;

    private String pickTaskId;

    private boolean qcTaskDone;

    private String itemBoxNum;

    private String allBoxNum;

    private String turnoverBoxNum;

    private String itemLineNum;

    private String collectionRoadCode;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public String getAllBoxNum() {
        return allBoxNum;
    }

    public void setAllBoxNum(String allBoxNum) {
        this.allBoxNum = allBoxNum;
    }

    public String getTurnoverBoxNum() {
        return turnoverBoxNum;
    }

    public void setTurnoverBoxNum(String turnoverBoxNum) {
        this.turnoverBoxNum = turnoverBoxNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getQcTaskId() {
        return qcTaskId;
    }

    public void setQcTaskId(String qcTaskId) {
        this.qcTaskId = qcTaskId;
    }

    public String getCollectionRoadCode() {
        return collectionRoadCode;
    }

    public void setCollectionRoadCode(String collectionRoadCode) {
        this.collectionRoadCode = collectionRoadCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPickTaskId() {
        return pickTaskId;
    }

    public void setPickTaskId(String pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

    public boolean isQcTaskDone() {
        return qcTaskDone;
    }

    public void setQcTaskDone(boolean qcTaskDone) {
        this.qcTaskDone = qcTaskDone;
    }

    public String getItemBoxNum() {
        return itemBoxNum;
    }

    public void setItemBoxNum(String itemBoxNum) {
        this.itemBoxNum = itemBoxNum;
    }

    public String getItemLineNum() {
        return itemLineNum;
    }

    public void setItemLineNum(String itemLineNum) {
        this.itemLineNum = itemLineNum;
    }

    public static class Item {

        boolean qcDone;

        boolean isSplit;

        String itemName;

        String itemId;

        String packName;

        String skuId;

        String codeType;

        String barCode;

        String uomQty;

        public boolean isSplit() {
            return isSplit;
        }

        public void setSplit(boolean split) {
            isSplit = split;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public boolean isQcDone() {
            return qcDone;
        }

        public void setQcDone(boolean qcDone) {
            this.qcDone = qcDone;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getUomQty() {
            return uomQty;
        }

        public void setUomQty(String uomQty) {
            this.uomQty = uomQty;
        }
    }
}
