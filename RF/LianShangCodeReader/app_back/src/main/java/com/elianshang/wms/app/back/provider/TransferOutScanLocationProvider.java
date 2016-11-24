package com.elianshang.wms.app.back.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.back.bean.TransferList;
import com.elianshang.wms.app.back.parser.TransferListParser;
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
public class TransferOutScanLocationProvider {

    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/back/in_storage/scanLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String soOtherId = "soOtherId";

    public static DataHull<TransferList> request(Context context, String uId, String uToken, String soOtherId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(TransferOutScanLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TransferOutScanLocationProvider.soOtherId, soOtherId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TransferListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TransferListParser(), 0);

        OkHttpHandler<TransferList> handler = new OkHttpHandler();
        DataHull<TransferList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
