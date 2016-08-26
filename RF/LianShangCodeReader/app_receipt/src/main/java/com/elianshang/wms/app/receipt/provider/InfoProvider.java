package com.elianshang.wms.app.receipt.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.receipt.bean.Info;
import com.elianshang.wms.app.receipt.parser.ReceiptGetOrderInfoParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class InfoProvider {

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

    private static final String _function = "/order/po/receipt/getorderinfo";

    private static final String uId = "uId";

    private static final String uToken = "uToken";

    /**
     * 物美订单编号(扫描获得)
     */
    private static final String orderOtherId = "orderOtherId";

    /**
     * 托盘码(扫描获得)
     */
    private static final String containerId = "containerId";

    /**
     * 商品国条(扫描获得)
     */
    private static final String barCode = "barCode";


    public static DataHull<Info> request(String uId, String uToken, String orderOtherId, String containerId, String barCode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(InfoProvider.app_key, ""));
        headers.add(new DefaultKVPBean(InfoProvider.platform, ""));
        headers.add(new DefaultKVPBean(InfoProvider.version, ""));
        headers.add(new DefaultKVPBean(InfoProvider.api_version, ""));
        headers.add(new DefaultKVPBean(InfoProvider.uId, uId));
        headers.add(new DefaultKVPBean(InfoProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(InfoProvider.orderOtherId, orderOtherId));
        params.add(new DefaultKVPBean(InfoProvider.containerId, containerId));
        params.add(new DefaultKVPBean(InfoProvider.barCode, barCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ReceiptGetOrderInfoParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ReceiptGetOrderInfoParser(), 0);

        OkHttpHandler<Info> handler = new OkHttpHandler();
        DataHull<Info> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
