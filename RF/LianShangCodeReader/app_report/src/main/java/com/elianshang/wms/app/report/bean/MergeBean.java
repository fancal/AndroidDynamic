package com.elianshang.wms.app.report.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

/**
 * Created by xfilshy on 17/2/22.
 */

public class MergeBean implements BaseBean {

    private ResponseState responseState;

    private String msg;

    private ArrayList<String> errCodes;

    public boolean isDone() {
        if (responseState != null) {
            return true;
        }

        return false;
    }

    public ResponseState getResponseState() {
        return responseState;
    }

    public void setResponseState(ResponseState responseState) {
        this.responseState = responseState;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<String> getErrCodes() {
        return errCodes;
    }

    public void setErrCodes(ArrayList<String> errCodes) {
        this.errCodes = errCodes;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
