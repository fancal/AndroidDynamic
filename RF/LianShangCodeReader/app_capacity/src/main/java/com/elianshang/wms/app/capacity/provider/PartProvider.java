package com.elianshang.wms.app.capacity.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.capacity.bean.ResponseState;
import com.elianshang.wms.app.capacity.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class PartProvider {

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

    private static final String _function = "inbound/location/splitBins";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String locationCode = "locationCode";

    private static final String serialNumber = "serialNumber";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String locationCode, String serialNumber) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(PartProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(PartProvider.platform, "2"));
        headers.add(new DefaultKVPBean(PartProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(PartProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(PartProvider.uId, uId));
        headers.add(new DefaultKVPBean(PartProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(PartProvider.locationCode, locationCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);
        headers.add(new DefaultKVPBean(PartProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));
        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
