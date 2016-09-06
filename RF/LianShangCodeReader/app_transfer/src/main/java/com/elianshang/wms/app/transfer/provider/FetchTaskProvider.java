package com.elianshang.wms.app.transfer.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.parser.StockTransferParser;
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
public class FetchTaskProvider {

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

    private static final String _function = "/inhouse/stock_transfer/fetchTask";


    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 操作员id
     */
    private static final String udd = "uId";


    public static DataHull<Transfer> request(Context context, String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(FetchTaskProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(FetchTaskProvider.platform, "2"));
        headers.add(new DefaultKVPBean(FetchTaskProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(FetchTaskProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(FetchTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(FetchTaskProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(FetchTaskProvider.udd, uId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StockTransferParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new StockTransferParser(), 0);

        OkHttpHandler<Transfer> handler = new OkHttpHandler();
        DataHull<Transfer> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
