package com.elianshang.wms.app.qc.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class QcList extends ArrayList<QcList.Item> implements BaseBean {

    private int qcType;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public int getQcType() {
        return qcType;
    }

    public void setQcType(int qcType) {
        this.qcType = qcType;
    }

    public static class Item {

        String itemName;

        String barCode;

        String packName;

        String qty;

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }
}
