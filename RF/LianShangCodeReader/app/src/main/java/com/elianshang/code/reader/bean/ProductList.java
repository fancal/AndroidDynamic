package com.elianshang.code.reader.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class ProductList extends ArrayList<Product> implements BaseBean {
    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
