package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class Pick implements BaseBean {

    String allocPickLocation;

    String allocPickLocationId;

    String allocCollectLocation;

    String allocCollectLocationId;

    String allocQty;

    String itemId;

    public String getAllocPickLocation() {
        return allocPickLocation;
    }

    public void setAllocPickLocation(String allocPickLocation) {
        this.allocPickLocation = allocPickLocation;
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

    public String getAllocCollectLocation() {
        return allocCollectLocation;
    }

    public void setAllocCollectLocation(String allocCollectLocation) {
        this.allocCollectLocation = allocCollectLocation;
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
