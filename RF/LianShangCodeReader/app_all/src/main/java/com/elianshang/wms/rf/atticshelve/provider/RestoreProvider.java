package com.elianshang.wms.rf.atticshelve.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.rf.atticshelve.bean.Restore;
import com.elianshang.wms.rf.atticshelve.parser.RestoreParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class RestoreProvider {

    private static final String base_url = "http://hd01.rf.wms.lsh123.wumart.com/api/wms/rf/v1";

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

    private static final String _function = "/inbound/pick_up_shelve/restore";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    public static DataHull<Restore> request(Context context , String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(RestoreProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(RestoreProvider.platform, "2"));
        headers.add(new DefaultKVPBean(RestoreProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(RestoreProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(RestoreProvider.uId, uId));
        headers.add(new DefaultKVPBean(RestoreProvider.uToken, uToken));

        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<RestoreParser> parameter = new HttpDynamicParameter<>(url, headers, null, type, new RestoreParser(), 0);

        OkHttpHandler<Restore> handler = new OkHttpHandler();
        DataHull<Restore> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
