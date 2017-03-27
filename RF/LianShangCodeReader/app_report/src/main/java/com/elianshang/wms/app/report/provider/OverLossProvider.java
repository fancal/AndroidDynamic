package com.elianshang.wms.app.report.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.report.bean.ResponseState;
import com.elianshang.wms.app.report.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class OverLossProvider {

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

    private static final String _function = "/inhouse/overLoss/scanLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String ownerId = "ownerId";

    private static final String locationCode = "locationCode";

    private static final String type = "type";

    private static final String code = "code";

    private static final String scatterQty = "scatterQty";

    private static final String umoQty = "umoQty";

    private static final String dueTime = "dueTime";

    private static final String serialNumber = "serialNumber";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String type, String locationCode, String code, String ownerId, String dueTime, String umoQty, String scatterQty, String serialNumber) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(OverLossProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(OverLossProvider.platform, "2"));
        headers.add(new DefaultKVPBean(OverLossProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(OverLossProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(OverLossProvider.uId, uId));
        headers.add(new DefaultKVPBean(OverLossProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(OverLossProvider.type, type));
        params.add(new DefaultKVPBean(OverLossProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(OverLossProvider.ownerId, ownerId));
        params.add(new DefaultKVPBean(OverLossProvider.code, code));
        params.add(new DefaultKVPBean(OverLossProvider.dueTime, dueTime));
        params.add(new DefaultKVPBean(OverLossProvider.umoQty, umoQty));
        params.add(new DefaultKVPBean(OverLossProvider.scatterQty, scatterQty));
        int requestType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, requestType, new ResponseStateParser(), 0);
        headers.add(new DefaultKVPBean(OverLossProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));
        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
