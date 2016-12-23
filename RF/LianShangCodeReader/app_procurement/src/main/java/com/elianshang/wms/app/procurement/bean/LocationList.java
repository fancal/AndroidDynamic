package com.elianshang.wms.app.procurement.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/12/8.
 */

public class LocationList implements BaseBean {

    private ArrayList<Location> list = new ArrayList();

    public void add(Location zone) {
        if (list == null) {
            return;
        }
        list.add(zone);
    }

    public void removew(Location zone) {
        if (list == null) {
            return;
        }
        list.remove(zone);
    }

    public Location get(int pos) {
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

    public static class Location {

        String taskId ;

        String locationCode;

        String priority ;

        String packCount ;

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

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getPackCount() {
            return packCount;
        }

        public void setPackCount(String packCount) {
            this.packCount = packCount;
        }
    }
}
