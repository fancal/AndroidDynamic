package com.elianshang.wms.app.takestock.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.parser.TakeStockListParser;
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
public class AssignProvider {

    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/inhouse/stock_taking/assign";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    public static DataHull<TakeStockList> request(Context context, String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(AssignProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(AssignProvider.platform, "2"));
        headers.add(new DefaultKVPBean(AssignProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(AssignProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(AssignProvider.uId, uId));
        headers.add(new DefaultKVPBean(AssignProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TakeStockListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TakeStockListParser(), 0);

        OkHttpHandler<TakeStockList> handler = new OkHttpHandler();
        DataHull<TakeStockList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
