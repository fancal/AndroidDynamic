package com.elianshang.wms.app.takestock.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;
import com.elianshang.wms.app.takestock.parser.TakeStockDetailParser;
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
public class GetTaskProvider {

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

    private static final String _function = "/inhouse/stock_taking/getTask";

    private static final String uId = "uid";

    private static final String uToken = "uToken";

    private static final String taskId = "taskId";

    private static final String locationId = "locationId";


    public static DataHull<TakeStockDetail> request(String uId, String uToken, String taskId, String locationId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(GetTaskProvider.app_key, ""));
        headers.add(new DefaultKVPBean(GetTaskProvider.platform, ""));
        headers.add(new DefaultKVPBean(GetTaskProvider.version, ""));
        headers.add(new DefaultKVPBean(GetTaskProvider.api_version, ""));
        headers.add(new DefaultKVPBean(GetTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(GetTaskProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(GetTaskProvider.taskId, taskId));
        params.add(new DefaultKVPBean(GetTaskProvider.locationId, locationId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TakeStockDetailParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TakeStockDetailParser(), 0);

        OkHttpHandler<TakeStockDetail> handler = new OkHttpHandler();
        DataHull<TakeStockDetail> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
