package com.elianshang.wms.app.qc.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.parser.QcListParser;
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

    private static final String base_url = "http://rf.wmdev.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/qc/scanContainer";

    private static final String uId = "uid";

    private static final String uToken = "uToken";

    private static final String containerId = "containerId";


    public static DataHull<QcList> request(String uId, String uToken, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanContainerProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.api_version, ""));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanContainerProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<QcListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new QcListParser(), 0);

        OkHttpHandler<QcList> handler = new OkHttpHandler();
        DataHull<QcList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
