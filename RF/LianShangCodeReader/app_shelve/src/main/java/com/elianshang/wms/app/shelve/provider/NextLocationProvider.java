package com.elianshang.wms.app.shelve.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.shelve.bean.NextLocation;
import com.elianshang.wms.app.shelve.parser.NextLocationParser;
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
public class NextLocationProvider {

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

    private static final String _function = "/inbound/shelve/getNextAllocLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    public static DataHull<NextLocation> request(Context context, String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(NextLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(NextLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(NextLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(NextLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(NextLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(NextLocationProvider.uToken, uToken));

        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<NextLocationParser> parameter = new HttpDynamicParameter<>(url, headers, null, type, new NextLocationParser(), 0);

        OkHttpHandler<NextLocation> handler = new OkHttpHandler();
        DataHull<NextLocation> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
