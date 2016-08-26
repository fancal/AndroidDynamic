package com.elianshang.wms.app.pick.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.pick.bean.PickLocation;
import com.elianshang.wms.app.pick.parser.PickLocationParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ScanPickLocationProvider {

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

    private static final String _function = "/outbound/pick/scanPickLocation";

    private static final String uId = "uid";

    private static final String uToken = "uToken";


    private static final String locationId = "locationId";

    private static final String qty = "qty";

    private static final String operator = "operator";


    public static DataHull<PickLocation> request(String uId, String uToken, String locationId, String qty) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.api_version, ""));
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanPickLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanPickLocationProvider.locationId, locationId));
        params.add(new DefaultKVPBean(ScanPickLocationProvider.qty, qty));
        params.add(new DefaultKVPBean(ScanPickLocationProvider.operator, uId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickLocationParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickLocationParser(), 0);

        OkHttpHandler<PickLocation> handler = new OkHttpHandler();
        DataHull<PickLocation> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
