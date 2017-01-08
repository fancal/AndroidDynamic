package com.elianshang.wms.app.atticshelve.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.atticshelve.bean.AtticShelve;
import com.elianshang.wms.app.atticshelve.parser.AtticShelveParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ScanContainerProvider {

    private static final String base_url = "http://static.qatest2.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/inbound/pick_up_shelve/scanContainer";

    private static final String uId = "uId";

    private static final String uToken = "utoken";

    private static final String containerId = "containerId";


    public static DataHull<AtticShelve> request(Context context, String uId, String uToken, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanContainerProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uToken, uToken));

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
