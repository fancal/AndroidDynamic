package com.elianshang.wms.app.sow.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.sow.bean.Sow;
import com.elianshang.wms.app.sow.parser.SowParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ViewProvider {

    private static final String base_url = "http://static.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/seed/view";

    private static final String uId = "uId";

    private static final String uToken = "utoken";

    private static final String taskId = "taskId";

    private static final String storeNo = "storeNo";


    public static DataHull<Sow> request(Context context, String uId, String uToken, String taskId, String storeNo) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ViewProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ViewProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ViewProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ViewProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ViewProvider.uId, uId));
        headers.add(new DefaultKVPBean(ViewProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ViewProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ViewProvider.storeNo, storeNo));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SowParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SowParser(), 0);

        OkHttpHandler<Sow> handler = new OkHttpHandler();
        DataHull<Sow> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
