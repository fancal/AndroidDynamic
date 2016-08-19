package com.elianshang.wms.app.shelve.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.shelve.bean.ResponseState;
import com.elianshang.wms.app.shelve.parser.ResponseStateParser;
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
public class ScanTargetLocationProvider {

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

    private static final String _function = "/inbound/shelve/scanTargetLocation";

    /**
     * 任务ID
     */
    private static final String taskId = "taskId";

    /**
     * 位置ID
     */
    private static final String locationId = "locationId";


    public static DataHull<ResponseState> request(String taskId, String locationId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.locationId, locationId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
