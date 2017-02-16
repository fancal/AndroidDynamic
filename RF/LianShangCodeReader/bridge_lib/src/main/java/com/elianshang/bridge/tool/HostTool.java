package com.elianshang.bridge.tool;

public class HostTool {

    public static HostElement curHost;

    public static final HostElement[] hosts = {
            new HostElement("杭州01", "http://hd01.rf.wms.lsh123.wumart.com/api/wms/rf/v1"),
            new HostElement("qatest2", "http:/static.qatest2.rf.lsh123.com/api/wms/rf/v1")
    };

    static {
        if (hosts.length == 1) {
            curHost = hosts[0];
        }
    }

    public static class HostElement {

        private String hostUrl;

        private String hostName;

        public HostElement(String hostName, String hostUrl) {
            this.hostName = hostName;
            this.hostUrl = hostUrl;
        }

        public String getHostUrl() {
            return hostUrl;
        }

        public void setHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }
    }

}