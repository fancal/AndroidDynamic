package com.elianshang.wms.app.qc.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.qc.bean.QCDoneState;
import com.elianshang.wms.app.qc.parser.QCDoneParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class DealCaseProvider {

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

    private static final String _function = "/outbound/qc/dealCase";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";

    private static final String containerId = "containerId";

    private static final String code = "code";

    private static final String uomQty = "uomQty";

    private static final String type = "type";

    public static DataHull<QCDoneState> request(Context context, String uId, String uToken, String containerId, String code, String uomQty, String type , String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(DealCaseProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(DealCaseProvider.platform, "2"));
        headers.add(new DefaultKVPBean(DealCaseProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(DealCaseProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(DealCaseProvider.uId, uId));
        headers.add(new DefaultKVPBean(DealCaseProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(DealCaseProvider.containerId, containerId));
        params.add(new DefaultKVPBean(DealCaseProvider.code, code));
        params.add(new DefaultKVPBean(DealCaseProvider.uomQty, uomQty));
        params.add(new DefaultKVPBean(DealCaseProvider.type, type));

        HttpDynamicParameter<QCDoneParser> parameter = new HttpDynamicParameter<>(url, headers, params, BaseHttpParameter.Type.POST, new QCDoneParser(), 0);

        headers.add(new DefaultKVPBean(DealCaseProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<QCDoneState> handler = new OkHttpHandler();
        DataHull<QCDoneState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
