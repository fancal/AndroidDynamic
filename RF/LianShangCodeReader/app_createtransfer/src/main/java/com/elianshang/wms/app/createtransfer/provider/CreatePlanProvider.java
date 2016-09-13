package com.elianshang.wms.app.createtransfer.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.createtransfer.bean.ResponseState;
import com.elianshang.wms.app.createtransfer.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class CreatePlanProvider {

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

    private static final String _function = "/inhouse/stock_transfer/createPlan";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 流水号
     */
    private static final String serialNumber = "serialNumber";


    private static final String type = "type";

    /**
     * 库位id
     */
    private static final String locationCode = "locationCode";

    /**
     * 数量
     */
    private static final String uomQty = "uomQty";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String locationCode, String uomQty, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(CreatePlanProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(CreatePlanProvider.platform, "2"));
        headers.add(new DefaultKVPBean(CreatePlanProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(CreatePlanProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(CreatePlanProvider.uId, uId));
        headers.add(new DefaultKVPBean(CreatePlanProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(CreatePlanProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(CreatePlanProvider.uomQty, uomQty));
        int hType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, hType, new ResponseStateParser(), 0);

        headers.add(new DefaultKVPBean(CreatePlanProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
