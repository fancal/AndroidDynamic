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

/**
 * Created by xfilshy on 16/8/18.
 */
public class OutboundPickScanPickLocationProvider {

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

    private static final String _function = "/outbound/pick/scanPickLocation";

    private static final String locationId = "locationId";

    private static final String qty = "qty";


    public static DataHull<PickLocation> request(String uId, String uToken, String locationId, String qty) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.locationId, locationId));
        params.add(new DefaultKVPBean(OutboundPickScanPickLocationProvider.qty, qty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickLocationParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickLocationParser(), 0);

        OkHttpHandler<PickLocation> handler = new OkHttpHandler();
        DataHull<PickLocation> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
