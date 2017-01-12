package com.elianshang.wms.app.view.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.view.bean.DataBean;
import com.elianshang.wms.app.view.parser.DataBeanParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class SearchProvider {

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

    private static final String _function = "/search/searchSomething";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String code = "code";


    public static DataHull<DataBean> request(Context context, String uId, String uToken, String code) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(SearchProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(SearchProvider.platform, "2"));
        headers.add(new DefaultKVPBean(SearchProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(SearchProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(SearchProvider.uId, uId));
        headers.add(new DefaultKVPBean(SearchProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(SearchProvider.code, code));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<DataBeanParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new DataBeanParser(), 0);

        OkHttpHandler<DataBean> handler = new OkHttpHandler();
        DataHull<DataBean> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
