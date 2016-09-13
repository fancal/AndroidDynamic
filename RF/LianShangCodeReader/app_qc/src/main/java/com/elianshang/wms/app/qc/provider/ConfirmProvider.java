package com.elianshang.wms.app.qc.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.qc.bean.ResponseState;
import com.elianshang.wms.app.qc.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ConfirmProvider {

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

    private static final String _function = "/outbound/qc/confirm";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";

    private static final String qcTaskId = "qcTaskId";

    private static final String boxNum = "boxNum";

    private static final String turnoverBoxNum = "turnoverBoxNum";

    private static final String wrongItemNum = "wrongItemNum";

    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String qcTaskId, String boxNum, String turnoverBoxNum, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ConfirmProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ConfirmProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ConfirmProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ConfirmProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ConfirmProvider.uId, uId));
        headers.add(new DefaultKVPBean(ConfirmProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ConfirmProvider.qcTaskId, qcTaskId));
        params.add(new DefaultKVPBean(ConfirmProvider.boxNum, boxNum));
        params.add(new DefaultKVPBean(ConfirmProvider.turnoverBoxNum, turnoverBoxNum));
        params.add(new DefaultKVPBean(ConfirmProvider.wrongItemNum, wrongItemNum));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);

        headers.add(new DefaultKVPBean(ConfirmProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
