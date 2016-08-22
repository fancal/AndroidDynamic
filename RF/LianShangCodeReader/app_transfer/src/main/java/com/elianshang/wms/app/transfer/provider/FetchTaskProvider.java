package com.elianshang.wms.app.transfer.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.transfer.bean.StockTransfer;
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

    private static final String _function = "/inhouse/stock_transfer/fetchTask";


    /**
     * 操作员id
     */
    private static final String uId = "uId";


    public static DataHull<StockTransfer> request(String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(FetchTaskProvider.app_key, ""));
        headers.add(new DefaultKVPBean(FetchTaskProvider.platform, ""));
        headers.add(new DefaultKVPBean(FetchTaskProvider.version, ""));
        headers.add(new DefaultKVPBean(FetchTaskProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(FetchTaskProvider.uId, uId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StockTransferParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new StockTransferParser(), 0);

        OkHttpHandler<StockTransfer> handler = new OkHttpHandler();
        DataHull<StockTransfer> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
