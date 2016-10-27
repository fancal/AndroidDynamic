package com.elianshang.wms.app.back.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.back.bean.ResponseState;
import com.elianshang.wms.app.back.parser.ResponseStateParser;
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
public class TransferInConfirmProvider {

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

    private static final String _function = "/back/in_storage/scanLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String locationCode = "locationCode";

    private static final String taskId = "taskId";

    private static final String serialNumber = "serialNumber";

    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String locationCode, String taskId, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.platform, "2"));
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.uId, uId));
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TransferInConfirmProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(TransferInConfirmProvider.taskId, taskId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);
        headers.add(new DefaultKVPBean(TransferInConfirmProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}