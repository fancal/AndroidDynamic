package com.elianshang.wms.app.releasecollection.bean;

import com.xue.http.hook.BaseBean;

/**
 * Created by xfilshy on 16/12/23.
 */

public class CollectionRoadDetail implements BaseBean {

    private String customerCount;

    private String packCount;

    private String turnoverBoxNum;

    private String transPlan ;

    private String driverName;

    private String carNumber;

    public String getTransPlan() {
        return transPlan;
    }

    public void setTransPlan(String transPlan) {
        this.transPlan = transPlan;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPackCount() {
        return packCount;
    }

    public void setPackCount(String packCount) {
        this.packCount = packCount;
    }

    public String getTurnoverBoxNum() {
        return turnoverBoxNum;
    }

    public void setTurnoverBoxNum(String turnoverBoxNum) {
        this.turnoverBoxNum = turnoverBoxNum;
    }

    public String getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(String customerCount) {
        this.customerCount = customerCount;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
