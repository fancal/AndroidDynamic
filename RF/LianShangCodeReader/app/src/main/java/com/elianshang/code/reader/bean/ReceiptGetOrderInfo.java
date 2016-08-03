package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/8/3.
 */
public class ReceiptGetOrderInfo implements BaseBean {

    private String skuName;

    private int packUnit;

    private int batchNeeded;

    private int orderQty;


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

    public int getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(int packUnit) {
        this.packUnit = packUnit;
    }

    public int getBatchNeeded() {
        return batchNeeded;
    }

    public void setBatchNeeded(int batchNeeded) {
        this.batchNeeded = batchNeeded;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }
}
