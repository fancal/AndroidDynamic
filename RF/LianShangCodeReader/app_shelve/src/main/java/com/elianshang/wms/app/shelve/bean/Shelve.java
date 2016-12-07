package com.elianshang.wms.app.shelve.bean;

import com.xue.http.hook.BaseBean;

public class Shelve implements BaseBean {

    private String id;

    private String taskId;

    private String receiptId;

    private String orderId;

    private String skuId;

    private String ownerId;

    private String lotId;

    private String supplierId;

    private String allocLocationCode;

    private String containerId;

    private String itemName;

    private String barcode;

    private String skuCode;

    private String pickLocationList;

    public String getPickLocationList() {
        return pickLocationList;
    }

    public void setPickLocationList(String pickLocationList) {
        this.pickLocationList = pickLocationList;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getAllocLocationCode() {
        return allocLocationCode;
    }

    public void setAllocLocationCode(String allocLocationCode) {
        this.allocLocationCode = allocLocationCode;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
