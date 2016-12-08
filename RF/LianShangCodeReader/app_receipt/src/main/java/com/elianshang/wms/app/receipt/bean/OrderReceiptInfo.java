package com.elianshang.wms.app.receipt.bean;

import com.xue.http.hook.BaseBean;

public class OrderReceiptInfo implements BaseBean {

    private String skuName;

    private String skuCode;

    private String barcode;

    private String packName;

    private int batchNeeded;

    private String orderQty;

    private String pile ;

    private int isNeedProTime ;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getIsNeedProTime() {
        return isNeedProTime;
    }

    public void setIsNeedProTime(int isNeedProTime) {
        this.isNeedProTime = isNeedProTime;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

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

    public int getBatchNeeded() {
        return batchNeeded;
    }

    public void setBatchNeeded(int batchNeeded) {
        this.batchNeeded = batchNeeded;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }
}
