package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
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
 * 领取任务接口
 */
public class ZoneLoginProvider {

    private static final String base_url = "http://hz01.rf.wms.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/inhouse/procurement/loginToZone";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String zoneId = "zoneId";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String zoneId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ZoneLoginProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ZoneLoginProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ZoneLoginProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ZoneLoginProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ZoneLoginProvider.uId, uId));
        headers.add(new DefaultKVPBean(ZoneLoginProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ZoneLoginProvider.zoneId, zoneId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
