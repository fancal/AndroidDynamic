package com.elianshang.wms.app.pick.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.pick.bean.Pick;
import com.elianshang.wms.app.pick.parser.PickParser;
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
public class OutboundPickScanPickProvider {

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

    private static final String _function = "/outbound/pick/scanPickTask";

    private static final String uId = "operator";

    private static final String taskList = "taskList";


    public static DataHull<Pick> request(String uId, String uToken, String taskList) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(OutboundPickScanPickProvider.app_key, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickProvider.platform, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickProvider.version, ""));
        headers.add(new DefaultKVPBean(OutboundPickScanPickProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(OutboundPickScanPickProvider.uId, uId));
        params.add(new DefaultKVPBean(OutboundPickScanPickProvider.taskList, taskList));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickParser(), 0);

        OkHttpHandler<Pick> handler = new OkHttpHandler();
        DataHull<Pick> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
