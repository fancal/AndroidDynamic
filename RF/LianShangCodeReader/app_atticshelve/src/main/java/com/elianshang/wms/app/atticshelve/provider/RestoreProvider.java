package com.elianshang.wms.app.atticshelve.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.atticshelve.bean.Restore;
import com.elianshang.wms.app.atticshelve.parser.RestoreParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfilshy on 16/8/18.
 */
public class RestoreProvider {

    private static final String base_url = "http://rf.wmdev.lsh123.com/api/wms/rf/v1";

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

    private static final String uToken = "uToken";

    private static final String operator = "uId";

    public static DataHull<Restore> request(String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(RestoreProvider.app_key, ""));
        headers.add(new DefaultKVPBean(RestoreProvider.platform, ""));
        headers.add(new DefaultKVPBean(RestoreProvider.version, ""));
        headers.add(new DefaultKVPBean(RestoreProvider.api_version, ""));
        headers.add(new DefaultKVPBean(RestoreProvider.uId, uId));
        headers.add(new DefaultKVPBean(RestoreProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(RestoreProvider.operator, uId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<RestoreParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new RestoreParser(), 0);

        OkHttpHandler<Restore> handler = new OkHttpHandler();
        DataHull<Restore> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
