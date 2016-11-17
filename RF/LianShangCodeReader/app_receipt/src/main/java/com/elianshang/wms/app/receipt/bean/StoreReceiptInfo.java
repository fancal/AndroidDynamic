package com.elianshang.wms.app.receipt.bean;

import com.xue.http.hook.BaseBean;

public class StoreReceiptInfo implements BaseBean {

    private String location ;

    private String proTime;

    private String packUnit ;

    private String skuName;

    private String barCode;

    private String packName;

    private String orderId;

    private String orderQty ;

    private int isNeedProTime ;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getProTime() {
        return proTime;
    }

    public void setProTime(String proTime) {
        this.proTime = proTime;
    }


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getIsNeedProTime() {
        return isNeedProTime;
    }

    public void setIsNeedProTime(int isNeedProTime) {
        this.isNeedProTime = isNeedProTime;
    }
}
