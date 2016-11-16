package com.elianshang.wms.app.pick.bean;

import com.xue.http.hook.BaseBean;

public class Pick implements BaseBean {


    private String allocPickLocationCode;

    private String allocCollectLocationCode;

    private String allocQty;

    private String itemId;

    private String itemName ;

    private String packName ;

    private String allocUnitName ;

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
