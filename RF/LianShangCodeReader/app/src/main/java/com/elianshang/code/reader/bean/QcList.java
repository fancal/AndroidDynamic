package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/8/8.
 */
public class QcList extends ArrayList<QcList.Item> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Item {

        String itemName;

        String barCode;

        String packName;

        float qty;

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

        public float getQty() {
            return qty;
        }

        public void setQty(float qty) {
            this.qty = qty;
        }
    }
}
