package com.elianshang.code.pick.tool;

class ConfigForTest implements Config {

    ConfigForTest() {
    }

    @Override
    public boolean isTestApi() {
        return true;
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public String getHttpBaseUrl() {
        return "http://rf.wmdev.lsh123.com/api/wms/rf/v1/";
    }

    @Override
    public String getAliPayNotifyUrl() {
        return "http://qa.market.wmdev2.lsh123.com/pay/alinotify";
    }

}
