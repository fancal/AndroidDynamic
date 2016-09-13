package com.elianshang.wms.app.takestock.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/8/4.
 */
public class TakeStockList implements BaseBean {

    private ArrayList<TakeStockTask> list;

    public TakeStockList() {
        list = new ArrayList<>();
    }

    public boolean add(TakeStockTask task) {
        return list.add(task);
    }

    public boolean remove(TakeStockTask task) {
        return list.remove(task);
    }

    public int size() {
        return list.size();
    }

    public TakeStockTask get(int index) {
        return list.get(index);
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class TakeStockTask implements BaseBean {

        private String taskId;

        private String locationCode;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getLocationCode() {
            return locationCode;
        }

        public void setLocationCode(String locationCode) {
            this.locationCode = locationCode;
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
