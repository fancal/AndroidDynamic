package com.elianshang.wms.app.atticshelve.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.atticshelve.bean.AtticShelveNext;
import com.elianshang.wms.app.atticshelve.parser.AtticShelveNextParser;
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

    private static final String _function = "/inbound/pick_up_shelve/scanTargetLocation";

    private static final String uId = "uId";

    private static final String uToken = "uToken";

    /**
     * 任务ID
     */
    private static final String taskId = "taskId";
    private static final String allocLocationId = "allocLocationId";
    private static final String realLocationId = "realLocationId";
    private static final String qty = "qty";


    public static DataHull<AtticShelveNext> request(String uid, String uToken, String taskId, String allocLocationId, String realLocationId, String qty) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.api_version, ""));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.uId, uid));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.allocLocationId, allocLocationId));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.realLocationId, realLocationId));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.qty, qty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<AtticShelveNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new AtticShelveNextParser(), 0);

        OkHttpHandler<AtticShelveNext> handler = new OkHttpHandler();
        DataHull<AtticShelveNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
