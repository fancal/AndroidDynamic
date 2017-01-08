package com.elianshang.wms.app.view.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class SkuBean implements BaseBean {

    private String itemName;

    private String barcode;

    private String skuCode;

    private String packCode;

    private ArrayList<LocationItem> locationList;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public ArrayList<LocationItem> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LocationItem> locationList) {
        this.locationList = locationList;
    }

    public static class LocationItem {

        private String locationCode;

        private String packName;

        private String owner;

        private String qty;

        public String getLocationCode() {
            return locationCode;
        }

        public void setLocationCode(String locationCode) {
            this.locationCode = locationCode;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
