package com.elianshang.wms.app.receipt.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class StoreReceiptInfo implements BaseBean {

    private String proTime;

    private String sumUnitQty;

    private String skuName;

    private String barCode;

    private String orderIds;

    private String packName;

    private String sumPackQty;

    private ArrayList<OrderInfo> orderInfos;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public String getProTime() {
        return proTime;
    }

    public void setProTime(String proTime) {
        this.proTime = proTime;
    }

    public String getSumUnitQty() {
        return sumUnitQty;
    }

    public void setSumUnitQty(String sumUnitQty) {
        this.sumUnitQty = sumUnitQty;
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

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getSumPackQty() {
        return sumPackQty;
    }

    public void setSumPackQty(String sumPackQty) {
        this.sumPackQty = sumPackQty;
    }

    public ArrayList<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(ArrayList<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public static class OrderInfo implements BaseBean {

        String createTime;

        String obdQty;

        String packName;

        String orderId;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getObdQty() {
            return obdQty;
        }

        public void setObdQty(String obdQty) {
            this.obdQty = obdQty;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
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
