package com.elianshang.code.pick.tool;


import com.elianshang.bridge.tool.SecretTool;
import com.elianshang.code.pick.BaseApplication;

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
        return "http://static.rf.lsh123.com/api/wms/rf/v1/";
//        return SecretTool.getBaseUrl(BaseApplication.get());
    }

    @Override
    public String getAliPayNotifyUrl() {
        return SecretTool.getAliPayNotifyUrl(BaseApplication.get());
    }
}
