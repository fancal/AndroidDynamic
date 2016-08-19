package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class Pick implements BaseBean {

    String allocPickLocationCode;

    String allocPickLocationId;

    String allocCollectLocationCode;

    String allocCollectLocationId;

    String allocQty;

    String itemId;

    public String getAllocPickLocationCode() {
        return allocPickLocationCode;
    }

    public void setAllocPickLocationCode(String allocPickLocationCode) {
        this.allocPickLocationCode = allocPickLocationCode;
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

    public String getAllocCollectLocationCode() {
        return allocCollectLocationCode;
    }

    public void setAllocCollectLocationCode(String allocCollectLocationCode) {
        this.allocCollectLocationCode = allocCollectLocationCode;
    }

    public String getAllocPickLocationId() {
        return allocPickLocationId;
    }

    public void setAllocPickLocationId(String allocPickLocationId) {
        this.allocPickLocationId = allocPickLocationId;
    }

    public String getAllocCollectLocationId() {
        return allocCollectLocationId;
    }

    public void setAllocCollectLocationId(String allocCollectLocationId) {
        this.allocCollectLocationId = allocCollectLocationId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
