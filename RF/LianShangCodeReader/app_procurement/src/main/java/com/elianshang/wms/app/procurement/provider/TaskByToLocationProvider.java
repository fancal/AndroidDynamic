package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.parser.ProcurementParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 领取任务接口
 */
public class TaskByToLocationProvider {

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

    private static final String _function = "/inhouse/procurement/getTaskByToLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String locationCode = "locationCode";


    public static DataHull<Procurement> request(Context context, String uId, String uToken, String locationCode) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(TaskByToLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TaskByToLocationProvider.locationCode, locationCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ProcurementParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ProcurementParser(), 0);

        OkHttpHandler<Procurement> handler = new OkHttpHandler();
        DataHull<Procurement> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
