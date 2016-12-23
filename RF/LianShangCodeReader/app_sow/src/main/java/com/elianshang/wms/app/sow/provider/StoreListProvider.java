package com.elianshang.wms.app.sow.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.sow.bean.StoreList;
import com.elianshang.wms.app.sow.parser.StoreListParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class StoreListProvider {

<<<<<<< HEAD
    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";
=======
    private static final String base_url = "http://static.qatest2.rf.lsh123.com/api/wms/rf/v1";
>>>>>>> RF_1.0_QATEST2

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

    private static final String _function = "/seed/getStoreList";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String barcode = "barcode";

    private static final String containerId = "containerId";

    private static final String orderId = "orderId";

    private static final String storeType = "storeType";

    public static DataHull<StoreList> request(Context context, String uId, String uToken, String barcode, String containerId, String orderId, String storeType) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(StoreListProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(StoreListProvider.platform, "2"));
        headers.add(new DefaultKVPBean(StoreListProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(StoreListProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(StoreListProvider.uId, uId));
        headers.add(new DefaultKVPBean(StoreListProvider.uToken, uToken));


        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(StoreListProvider.barcode, barcode));
        params.add(new DefaultKVPBean(StoreListProvider.containerId, containerId));
        params.add(new DefaultKVPBean(StoreListProvider.orderId, orderId));
        params.add(new DefaultKVPBean(StoreListProvider.storeType, storeType));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StoreListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new StoreListParser(), 0);

        OkHttpHandler<StoreList> handler = new OkHttpHandler();
        DataHull<StoreList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
