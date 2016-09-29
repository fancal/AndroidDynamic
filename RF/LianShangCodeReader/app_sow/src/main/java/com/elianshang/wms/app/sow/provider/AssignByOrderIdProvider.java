package com.elianshang.wms.app.sow.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.sow.bean.Sow;
import com.elianshang.wms.app.sow.parser.SowParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class AssignByOrderIdProvider {

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

    private static final String _function = "/inbound/seed/assign";

    private static final String uId = "uId";

    private static final String uToken = "utoken";

    private static final String orderId = "orderId";

    private static final String barcode = "barcode";


    public static DataHull<Sow> request(Context context, String uId, String uToken, String orderId,String barcode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.platform, "2"));
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.uId, uId));
        headers.add(new DefaultKVPBean(AssignByOrderIdProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(AssignByOrderIdProvider.uId, uId));
        params.add(new DefaultKVPBean(AssignByOrderIdProvider.orderId, orderId));
        params.add(new DefaultKVPBean(AssignByOrderIdProvider.barcode, barcode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SowParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SowParser(), 0);

        OkHttpHandler<Sow> handler = new OkHttpHandler();
        DataHull<Sow> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
