package com.elianshang.wms.app.receipt.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.receipt.bean.StoreReceiptInfo;
import com.elianshang.wms.app.receipt.parser.ReceiptGetStoreInfoParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class StoreInfoProvider {

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

    private static final String _function = "/order/po/receipt/getStoreInfo";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 物美订单编号(扫描获得)
     */
    private static final String storeId = "storeId";

    /**
     * 托盘码(扫描获得)
     */
    private static final String containerId = "containerId";

    /**
     * 订单号(扫描获得)
     */
    private static final String orderOtherId = "orderOtherId";

    /**
     * 商品国条(扫描获得)
     */
    private static final String barCode = "barCode";


    public static DataHull<StoreReceiptInfo> request(Context context, String uId, String uToken, String storeId, String containerId, String orderOtherId, String barCode) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(StoreInfoProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(StoreInfoProvider.platform, "2"));
        headers.add(new DefaultKVPBean(StoreInfoProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(StoreInfoProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(StoreInfoProvider.uId, uId));
        headers.add(new DefaultKVPBean(StoreInfoProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(StoreInfoProvider.storeId, storeId));
        params.add(new DefaultKVPBean(StoreInfoProvider.containerId, containerId));
        params.add(new DefaultKVPBean(StoreInfoProvider.orderOtherId, orderOtherId));
        params.add(new DefaultKVPBean(StoreInfoProvider.barCode, barCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ReceiptGetStoreInfoParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ReceiptGetStoreInfoParser(), 0);

        OkHttpHandler<StoreReceiptInfo> handler = new OkHttpHandler();
        DataHull<StoreReceiptInfo> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
