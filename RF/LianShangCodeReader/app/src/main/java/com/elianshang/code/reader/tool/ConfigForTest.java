package com.elianshang.code.reader.tool;

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
        return "http://static.rf.lsh123.com/api/wms/rf/";
    }

    @Override
    public String getAliPayNotifyUrl() {
        return "http://qa.market.wmdev2.lsh123.com/pay/alinotify";
    }

}
