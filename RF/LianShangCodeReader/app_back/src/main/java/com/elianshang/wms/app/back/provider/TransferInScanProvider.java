package com.elianshang.wms.app.back.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.back.bean.SupplierInfo;
import com.elianshang.wms.app.back.parser.SupplierInfoParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class TransferInScanProvider {

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

    private static final String _function = "/back/in_storage/getSupplierInfo";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String soOtherId = "soOtherId";

    public static DataHull<SupplierInfo> request(Context context, String uId, String uToken, String soOtherId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TransferInScanProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TransferInScanProvider.platform, "2"));
        headers.add(new DefaultKVPBean(TransferInScanProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TransferInScanProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(TransferInScanProvider.uId, uId));
        headers.add(new DefaultKVPBean(TransferInScanProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TransferInScanProvider.soOtherId, soOtherId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SupplierInfoParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SupplierInfoParser(), 0);

        OkHttpHandler<SupplierInfo> handler = new OkHttpHandler();
        DataHull<SupplierInfo> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
