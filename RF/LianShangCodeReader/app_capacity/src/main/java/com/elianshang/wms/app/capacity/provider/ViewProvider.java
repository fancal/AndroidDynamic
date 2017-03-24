package com.elianshang.wms.app.capacity.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.capacity.bean.PartBean;
import com.elianshang.wms.app.capacity.parser.PartBeanParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ViewProvider {

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

    private static final String _function = "inbound/location/checkBin";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String locationCode = "locationCode";


    public static DataHull<PartBean> request(Context context, String uId, String uToken, String locationCode) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ViewProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ViewProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ViewProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ViewProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ViewProvider.uId, uId));
        headers.add(new DefaultKVPBean(ViewProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ViewProvider.locationCode, locationCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PartBeanParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PartBeanParser(), 0);
        OkHttpHandler<PartBean> handler = new OkHttpHandler();
        DataHull<PartBean> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}