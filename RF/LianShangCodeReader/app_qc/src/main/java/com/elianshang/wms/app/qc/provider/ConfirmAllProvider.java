package com.elianshang.wms.app.qc.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.qc.bean.ResponseState;
import com.elianshang.wms.app.qc.parser.ResponseStateParser;
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
public class ConfirmAllProvider {

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

    private static final String _function = "/outbound/qc/confirmAll";

    private static final String uId = "uid";

    private static final String uToken = "uToken";

    private static final String containerId = "containerId";

    private static final String qcList = "qcList";


    public static DataHull<ResponseState> request(Context context , String uId, String uToken, String containerId, String qcList) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ConfirmAllProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ConfirmAllProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ConfirmAllProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ConfirmAllProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ConfirmAllProvider.uId, uId));
        headers.add(new DefaultKVPBean(ConfirmAllProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ConfirmAllProvider.containerId, containerId));
        params.add(new DefaultKVPBean(ConfirmAllProvider.qcList, qcList));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
