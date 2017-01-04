package com.elianshang.wms.app.load.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.load.bean.ResponseState;
import com.elianshang.wms.app.load.parser.ResponseStateParser;
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
public class ConfirmProvier {

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

    private static final String _function = "/outbound/load/confirm";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String tuId = "tuId";

    private static final String serialNumber = "serialNumber";

    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String tuId,String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ConfirmProvier.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ConfirmProvier.platform, "2"));
        headers.add(new DefaultKVPBean(ConfirmProvier.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ConfirmProvier.api_version, "v1"));
        headers.add(new DefaultKVPBean(ConfirmProvier.uId, uId));
        headers.add(new DefaultKVPBean(ConfirmProvier.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ConfirmProvier.tuId, tuId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);
        headers.add(new DefaultKVPBean(ConfirmProvier.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
