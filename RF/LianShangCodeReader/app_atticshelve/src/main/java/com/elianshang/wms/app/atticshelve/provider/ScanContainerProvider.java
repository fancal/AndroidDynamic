package com.elianshang.wms.app.atticshelve.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.atticshelve.bean.AtticShelve;
import com.elianshang.wms.app.atticshelve.parser.AtticShelveParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfilshy on 16/8/18.
 */
public class ScanContainerProvider {

    private static final String base_url = "http://static.rf.lsh123.com/api/wms/rf/v1";

    /**
     * app唯一标示传imei
     */
    private static final String app_key = "app-key";

    /**
     * 平台(1.H5  2.Android)
     */
    private static final String platform = "platform";

    /**
     * app版本
     */
    private static final String version = "app-version";

    /**
     * api版本
     */
    private static final String api_version = "api-version";

    private static final String _function = "/inbound/attic_shelve/scanContainer";

    /**
     * 任务ID
     */
    private static final String uId = "uId";

    private static final String containerId = "containerId";


    public static DataHull<AtticShelve> request(String uId, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanContainerProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanContainerProvider.uId, uId));
        params.add(new DefaultKVPBean(ScanContainerProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<AtticShelveParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new AtticShelveParser(), 0);

        OkHttpHandler<AtticShelve> handler = new OkHttpHandler();
        DataHull<AtticShelve> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
