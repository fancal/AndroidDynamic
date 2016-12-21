package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.procurement.bean.ZoneList;
import com.elianshang.wms.app.procurement.parser.ZoneListParser;
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
public class ZoneListProvider {

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

    private static final String _function = "/inhouse/procurement/getZoneList";

    private static final String uId = "uid";

    private static final String uToken = "utoken";



    public static DataHull<ZoneList> request(Context context, String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ZoneListProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ZoneListProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ZoneListProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ZoneListProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ZoneListProvider.uId, uId));
        headers.add(new DefaultKVPBean(ZoneListProvider.uToken, uToken));

        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ZoneListParser> parameter = new HttpDynamicParameter<>(url, headers, null, type, new ZoneListParser(), 0);

        OkHttpHandler<ZoneList> handler = new OkHttpHandler();
        DataHull<ZoneList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
