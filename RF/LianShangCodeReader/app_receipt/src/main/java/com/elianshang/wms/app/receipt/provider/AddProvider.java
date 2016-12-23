package com.elianshang.wms.app.receipt.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.receipt.bean.ResponseState;
import com.elianshang.wms.app.receipt.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class AddProvider {

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

    private static final String _function = "/order/po/receipt/add";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 流水号
     */
    private static final String serialNumber = "serialNumber";

    /**
     * 物美门店ID
     */
    private static final String storeId = "storeId";

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

//    /**
//     * 批次号
//     */
//    private static final String items_lotNum = "lotNum";
//
//    /**
//     * 商品国条码
//     */
//    private static final String barCode = "barCode";
//
//    /**
//     * 实际收货数
//     */
//    private static final String items_inboundQty = "inboundQty";
//
//    /**
//     * 生产日期
//     */
//    private static final String items_proTime = "proTime";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String storeId, String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(AddProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(AddProvider.platform, "2"));
        headers.add(new DefaultKVPBean(AddProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(AddProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(AddProvider.uId, uId));
        headers.add(new DefaultKVPBean(AddProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(AddProvider.storeId, storeId));
        params.add(new DefaultKVPBean(AddProvider.orderOtherId, orderOtherId));
        params.add(new DefaultKVPBean(AddProvider.containerId, containerId));
        params.add(new DefaultKVPBean(AddProvider.bookingNum, bookingNum));
        params.add(new DefaultKVPBean(AddProvider.receiptWharf, receiptWharf));
        params.add(new DefaultKVPBean(AddProvider.items, items));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        headers.add(new DefaultKVPBean(AddProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
