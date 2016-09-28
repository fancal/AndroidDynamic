package com.elianshang.wms.app.mergeboard.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class BoardDetailList extends ArrayList<BoardDetailList.BoardDetail> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class BoardDetail implements BaseBean{

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }

    }
}
