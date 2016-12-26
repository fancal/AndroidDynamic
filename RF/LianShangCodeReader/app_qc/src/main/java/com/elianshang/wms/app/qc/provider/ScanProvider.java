package com.elianshang.wms.app.qc.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.parser.QcListParser;
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
public class ScanProvider {

    private static final String base_url = "http://hz01.rf.wms.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/qc/scan";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String code = "code";


    public static DataHull<QcList> request(Context context, String uId, String uToken, String code) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanProvider.code, code));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<QcListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new QcListParser(), 0);

        OkHttpHandler<QcList> handler = new OkHttpHandler();
        DataHull<QcList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
