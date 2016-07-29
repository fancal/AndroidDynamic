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
//        return "http://wl.market.wmdev2.lsh123.com/";//吴淋服务器
//        return "http://hx.market-rd.wmdev2.lsh123.com/";//华旭服务器
//        return "http://gl.market-rd.wmdev2.lsh123.com/"; //丽丽服务器
//        return "http://lw.market-rd.wmdev2.lsh123.com/"; //刘威服务器
//        return "http://ys.market-rd.wmdev2.lsh123.com/"; //杨帅服务器
//        return "http://lsy.rd.wmdev2.lsh123.com/"; //李少永服务器
//        return "http://wc.market-rd.wmdev2.lsh123.com/"; //王超服务器
        return "http://qa.market.wmdev2.lsh123.com/";
//        return "https://market.lsh123.com/";
    }

    @Override
    public String getAliPayNotifyUrl() {
        return "http://qa.market.wmdev2.lsh123.com/pay/alinotify";
    }

}
