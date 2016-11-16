package com.elianshang.wms.app.load.bean;

import com.xue.http.hook.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuList extends ArrayList<TuList.Item> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Item implements Serializable{

        String tu;

        String carNumber;

        String preBoard;

        String number;

        String cellphone;

        ArrayList<Store> stores;

        String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTu() {
            return tu;
        }

        public void setTu(String tu) {
            this.tu = tu;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getPreBoard() {
            return preBoard;
        }

        public void setPreBoard(String preBoard) {
            this.preBoard = preBoard;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public ArrayList<Store> getStores() {
            return stores;
        }

        public void setStores(ArrayList<Store> stores) {
            this.stores = stores;
        }

        public static class Store implements Serializable {

            String customerName;

            String customerId;

            String customerCode;

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getCustomerCode() {
                return customerCode;
            }

            public void setCustomerCode(String customerCode) {
                this.customerCode = customerCode;
            }
        }

    }


}
