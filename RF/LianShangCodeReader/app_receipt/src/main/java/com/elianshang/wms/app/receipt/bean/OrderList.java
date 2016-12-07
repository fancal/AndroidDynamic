package com.elianshang.wms.app.receipt.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/12/7.
 */

public class OrderList implements BaseBean {

    private ArrayList<String> list = new ArrayList();

    public void add(String order) {
        if (list == null) {
            return;
        }

        list.add(order);
    }

    public void remove(String order) {
        if (list == null) {
            return;
        }

        list.remove(order);
    }

    public String get(int pos) {
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
}
