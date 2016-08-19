package com.elianshang.wms.app.procurement.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.procurement.bean.ResponseState;
import com.elianshang.wms.app.procurement.parser.ResponseStateParser;
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
public class ScanFromLocationProvider {

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

    private static final String _function = "v1/inhouse/procurement/scanFromLocation";

    /**
     * 任务id
     */
    private static final String taskId = "taskId";

    /**
     * 库位id
     */
    private static final String locationId = "locationId";

    /**
     * 操作员id
     */
    private static final String uId = "uId";

    /**
     * 数量
     */
    private static final String uomQty = "uomQty";


    public static DataHull<ResponseState> request(String taskId, String locationId, String uId, String uomQty) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanFromLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanFromLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanFromLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanFromLocationProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanFromLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanFromLocationProvider.locationId, locationId));
        params.add(new DefaultKVPBean(ScanFromLocationProvider.uId, uId));
        params.add(new DefaultKVPBean(ScanFromLocationProvider.uomQty, uomQty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
