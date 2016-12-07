package com.elianshang.wms.app.receipt.bean;

import com.xue.http.hook.BaseBean;

public class OrderReceiptInfo implements BaseBean {

    private String skuName;

    private String packName;

    private int batchNeeded;

    private String orderQty;

    private String pile ;

    private int isNeedProTime ;


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
