package com.elianshang.wms.app.back.bean;

import com.xue.http.hook.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

public class BackList implements BaseBean {

    private ArrayList<Item> list;

    public BackList() {
        list = new ArrayList();
    }

    public void add(Item item) {
        if (list == null) {
            return;
        }

        list.add(item);
    }

    public void remove(Item item) {
        if (list == null) {
            return;
        }

        list.remove(item);
    }

    public void remove(int pos) {
        if (list == null) {
            return;
        }

        list.remove(pos);
    }

    public Item get(int pos) {
        if (list == null) {
            return null;
        }

        return list.get(pos);
    }

    public int size() {
        if (list == null) {
            return 0;
        }

        return list.size();
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }


    public static class Item implements Serializable {

        String skuName;

        String barcode;

        String skuCode;

        String packCode;

        String packName;

        String locationCode;

        public String getLocationCode() {
            return locationCode;
        }

        public void setLocationCode(String locationCode) {
            this.locationCode = locationCode;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
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

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }
    }
}
