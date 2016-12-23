package com.elianshang.wms.app.releasecollection.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.releasecollection.bean.CollectionRoadDetail;
import com.elianshang.wms.app.releasecollection.parser.CollectionRoadDetailParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class CollectionRoadDetailProvider {

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

    private static final String _function = "/outbound/ship/showCollectionInfo";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 库位id
     */
    private static final String locationCode = "locationCode";

    public static DataHull<CollectionRoadDetail> request(Context context, String uId, String uToken, String locationCode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.platform, "2"));
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.uId, uId));
        headers.add(new DefaultKVPBean(CollectionRoadDetailProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(CollectionRoadDetailProvider.locationCode, locationCode));
        int hType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<CollectionRoadDetailParser> parameter = new HttpDynamicParameter<>(url, headers, params, hType, new CollectionRoadDetailParser(), 0);

        OkHttpHandler<CollectionRoadDetail> handler = new OkHttpHandler();
        DataHull<CollectionRoadDetail> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
