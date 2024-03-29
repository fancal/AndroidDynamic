package com.elianshang.wms.app.transfer.bean;


import com.xue.http.hook.BaseBean;

public class LocationView implements BaseBean {

    private String itemId;

    private String itemName;

    private String locationCode;

    private String lotId;

    private String uomQty;

    private String uom ;

    private String packName;

    private String barCode ;

    private String skuCode ;

    private String needBarcode;

    private String needOwner;

    private String owner ;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNeedBarcode() {
        return needBarcode;
    }

    public void setNeedBarcode(String needBarcode) {
        this.needBarcode = needBarcode;
    }

    public String getNeedOwner() {
        return needOwner;
    }

    public void setNeedOwner(String needOwner) {
        this.needOwner = needOwner;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getUomQty() {
        return uomQty;
    }

    public void setUomQty(String uomQty) {
        this.uomQty = uomQty;
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
