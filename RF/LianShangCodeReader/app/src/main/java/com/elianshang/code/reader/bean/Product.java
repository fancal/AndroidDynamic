package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class Product implements BaseBean {

    String name;
    ArrayList<String> packName;
    String itemId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPackName() {
        return packName;
    }

    public void setPackName(ArrayList<String> packName) {
        this.packName = packName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
