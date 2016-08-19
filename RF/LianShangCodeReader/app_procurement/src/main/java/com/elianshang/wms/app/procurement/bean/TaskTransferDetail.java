package com.elianshang.wms.app.procurement.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/4.
 */
public class TaskTransferDetail implements BaseBean {

    String taskId;

    String fromLocationId;

    String fromLocationName;

    String toLocationId;

    String toLocationName;

    String productId;

    /**
     * 商品名称
     */
    String productName;
    /**
     * 商品包装单位
     */
    String productPackName;

    String uomQty;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPackName() {
        return productPackName;
    }

    public void setProductPackName(String productPackName) {
        this.productPackName = productPackName;
    }

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public String getToLocationName() {
        return toLocationName;
    }

    public void setToLocationName(String toLocationName) {
        this.toLocationName = toLocationName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUomQty() {
        return uomQty;
    }

    public void setUomQty(String uomQty) {
        this.uomQty = uomQty;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
