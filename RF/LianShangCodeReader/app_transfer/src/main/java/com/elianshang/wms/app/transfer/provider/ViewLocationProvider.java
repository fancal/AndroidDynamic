package com.elianshang.wms.app.transfer.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.transfer.bean.LocationView;
import com.elianshang.wms.app.transfer.parser.LocationViewParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ViewLocationProvider {

    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/inhouse/transfer/viewLocation";


    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String locationCode = "locationCode";

    private static final String barcode = "barcode";

    private static final String owner = "owner";

    public static DataHull<LocationView> request(Context context, String uId, String uToken, String locationCode, String barcode, String owner) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ViewLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ViewLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ViewLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ViewLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ViewLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(ViewLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ViewLocationProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(ViewLocationProvider.barcode, barcode));
        params.add(new DefaultKVPBean(ViewLocationProvider.owner, owner));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<LocationViewParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new LocationViewParser(), 0);

        OkHttpHandler<LocationView> handler = new OkHttpHandler();
        DataHull<LocationView> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
