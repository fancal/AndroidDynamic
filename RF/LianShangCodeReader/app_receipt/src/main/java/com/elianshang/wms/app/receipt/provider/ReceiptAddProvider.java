package com.elianshang.wms.app.receipt.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.receipt.bean.ResponseState;
import com.elianshang.wms.app.receipt.parser.ResponseStateParser;
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
public class ReceiptAddProvider {

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

    private static final String _function = "/order/po/receipt/add";

    /**
     * 物美订单编号
     */
    private static final String orderOtherId = "orderOtherId";

    /**
     * 预约单号
     */
    private static final String bookingNum = "bookingNum";

    /**
     * 托盘码(扫描获得)
     */
    private static final String containerId = "containerId";

    /**
     * 收货码头
     */
    private static final String receiptWharf = "receiptWharf";

    /**
     * 收货明细数组
     */
    private static final String items = "items";

    /**
     * 批次号
     */
    private static final String items_lotNum = "lotNum";

    /**
     * 商品国条码
     */
    private static final String barCode = "barCode";

    /**
     * 实际收货数
     */
    private static final String items_inboundQty = "inboundQty";

    /**
     * 生产日期
     */
    private static final String items_proTime = "proTime";


    public static DataHull<ResponseState> request(String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ReceiptAddProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ReceiptAddProvider.platform, ""));
        headers.add(new DefaultKVPBean(ReceiptAddProvider.version, ""));
        headers.add(new DefaultKVPBean(ReceiptAddProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ReceiptAddProvider.orderOtherId, orderOtherId));
        params.add(new DefaultKVPBean(ReceiptAddProvider.containerId, containerId));
        params.add(new DefaultKVPBean(ReceiptAddProvider.bookingNum, bookingNum));
        params.add(new DefaultKVPBean(ReceiptAddProvider.receiptWharf, receiptWharf));
        params.add(new DefaultKVPBean(ReceiptAddProvider.items, items));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
