package com.elianshang.wms.app.qc.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.qc.bean.QcCreate;
import com.elianshang.wms.app.qc.parser.QcCreateParser;
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
public class CreateTaskProvider {

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

    private static final String _function = "/outbound/qc/createTask";

    private static final String containerId = "containerId";


    public static DataHull<QcCreate> request(String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(CreateTaskProvider.app_key, ""));
        headers.add(new DefaultKVPBean(CreateTaskProvider.platform, ""));
        headers.add(new DefaultKVPBean(CreateTaskProvider.version, ""));
        headers.add(new DefaultKVPBean(CreateTaskProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(CreateTaskProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<QcCreateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new QcCreateParser(), 0);

        OkHttpHandler<QcCreate> handler = new OkHttpHandler();
        DataHull<QcCreate> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
