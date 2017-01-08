package com.elianshang.wms.app.mergeboard.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.mergeboard.bean.CheckMerge;
import com.elianshang.wms.app.mergeboard.parser.CheckMergeParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckMergeContainersProvider {

    private static final String base_url = "http://hd01.wms.lsh123.wumart.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/merge/checkMergeContainers";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String containerIds = "containerIds";


    public static DataHull<CheckMerge> request(Context context, String uId, String uToken, String containerIds) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.platform, "2"));
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.uId, uId));
        headers.add(new DefaultKVPBean(CheckMergeContainersProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(CheckMergeContainersProvider.containerIds, containerIds));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<CheckMergeParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new CheckMergeParser(), 0);

        OkHttpHandler<CheckMerge> handler = new OkHttpHandler();
        DataHull<CheckMerge> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
