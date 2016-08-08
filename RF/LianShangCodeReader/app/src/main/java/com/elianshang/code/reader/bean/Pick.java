package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by liuhanzhi on 16/8/6.
 */
public class Pick implements BaseBean {

    String allocPickLocation;

    String allocCollectLocation;

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

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
