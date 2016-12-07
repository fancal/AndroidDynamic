package com.elianshang.wms.app.procurement.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/12/5.
 */

public class ShelfList extends ArrayList<ShelfList.Item> implements BaseBean{

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Item implements  BaseBean{

        String name;

        String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}
