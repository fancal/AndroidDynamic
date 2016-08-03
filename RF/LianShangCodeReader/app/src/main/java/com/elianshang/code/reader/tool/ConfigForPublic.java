package com.elianshang.code.reader.tool;

import com.elianshang.code.reader.BaseApplication;

class ConfigForPublic implements Config {

    ConfigForPublic() {
    }

    @Override
    public boolean isTestApi() {
        return false;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public String getHttpBaseUrl() {
        return "http://static.rf.lsh123.com/api/wms/rf/";
//        return SecretTool.getBaseUrl(BaseApplication.get());
    }

    @Override
    public String getAliPayNotifyUrl() {
        return SecretTool.getAliPayNotifyUrl(BaseApplication.get());
    }
}
