package com.elianshang.wms.app.transfer.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.transfer.bean.TransferNext;
import com.elianshang.wms.app.transfer.parser.StockTransferNextParser;
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
public class ScanLocationProvider {

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

    private static final String _function = "/inhouse/stock_transfer/scanLocation";

    private static final String uId = "uid";

    private static final String uToken = "uToken";


    private static final String type = "type";

    /**
     * 任务id
     */
    private static final String taskId = "taskId";

    /**
     * 库位id
     */
    private static final String locationId = "locationId";

    /**
     * 操作员id
     */
    private static final String udd = "uId";

    /**
     * 数量
     */
    private static final String uomQty = "uomQty";


    public static DataHull<TransferNext> request(String uId, String uToken, String type, String taskId, String locationId, String uomQty) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanLocationProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ScanLocationProvider.platform, ""));
        headers.add(new DefaultKVPBean(ScanLocationProvider.version, ""));
        headers.add(new DefaultKVPBean(ScanLocationProvider.api_version, ""));
        headers.add(new DefaultKVPBean(ScanLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanLocationProvider.type, type));
        params.add(new DefaultKVPBean(ScanLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanLocationProvider.locationId, locationId));
        params.add(new DefaultKVPBean(ScanLocationProvider.udd, uId));
        params.add(new DefaultKVPBean(ScanLocationProvider.uomQty, uomQty));
        int hType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StockTransferNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, hType, new StockTransferNextParser(), 0);

        OkHttpHandler<TransferNext> handler = new OkHttpHandler();
        DataHull<TransferNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
