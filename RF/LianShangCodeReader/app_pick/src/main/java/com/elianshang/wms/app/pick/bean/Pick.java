package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

public class Pick implements BaseBean {


    private String allocPickLocationCode;

    private String allocCollectLocationCode;

    private String allocQty;

    private String itemId;

    private String itemName;

    private String packName;

    private String allocUnitName;

    private String pickTaskId;

    private String containerId;

    private String barcode;

    private String skuCode;

    private String packCode;

    private String pickOrder ;

    public String getPickOrder() {
        return pickOrder;
    }

    public void setPickOrder(String pickOrder) {
        this.pickOrder = pickOrder;
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getPickTaskId() {
        return pickTaskId;
    }

    public void setPickTaskId(String pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

    public String getAllocUnitName() {
        return allocUnitName;
    }

    public void setAllocUnitName(String allocUnitName) {
        this.allocUnitName = allocUnitName;
    }

    public String getAllocQty() {
        return allocQty;
    }

    public void setAllocQty(String allocQty) {
        this.allocQty = allocQty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAllocPickLocationCode() {
        return allocPickLocationCode;
    }

    public void setAllocPickLocationCode(String allocPickLocationCode) {
        this.allocPickLocationCode = allocPickLocationCode;
    }

    public String getAllocCollectLocationCode() {
        return allocCollectLocationCode;
    }

    public void setAllocCollectLocationCode(String allocCollectLocationCode) {
        this.allocCollectLocationCode = allocCollectLocationCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPackName() {
        return packName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
