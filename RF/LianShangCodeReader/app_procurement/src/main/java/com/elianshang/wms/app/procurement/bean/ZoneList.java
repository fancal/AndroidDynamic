package com.elianshang.wms.app.procurement.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 16/12/8.
 */

public class ZoneList implements BaseBean {

    private ArrayList<Zone> list = new ArrayList();

    public void add(Zone zone) {
        if (list == null) {
            return;
        }
        list.add(zone);
    }

    public void removew(Zone zone) {
        if (list == null) {
            return;
        }
        list.remove(zone);
    }

    public Zone get(int pos) {
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

    public static class Zone {

        String zoneName;

        String workerNum;

        String taskNum;

        String zoneId;

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }

        public String getWorkerNum() {
            return workerNum;
        }

        public void setWorkerNum(String workerNum) {
            this.workerNum = workerNum;
        }

        public String getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(String taskNum) {
            this.taskNum = taskNum;
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }
    }
}
