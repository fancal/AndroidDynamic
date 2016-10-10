package com.elianshang.wms.app.takestock.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
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

    private static final String _function = "/inhouse/stock_taking/getTask";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String taskId = "taskId";

    private static final String locationCode = "locationCode";


    public static DataHull<TakeStockDetail> request(Context context, String uId, String uToken, String taskId, String locationCode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(GetTaskProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(GetTaskProvider.platform, "2"));
        headers.add(new DefaultKVPBean(GetTaskProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(GetTaskProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(GetTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(GetTaskProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(GetTaskProvider.taskId, taskId));
        params.add(new DefaultKVPBean(GetTaskProvider.locationCode, locationCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TakeStockDetailParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TakeStockDetailParser(), 0);

        OkHttpHandler<TakeStockDetail> handler = new OkHttpHandler();
        DataHull<TakeStockDetail> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
