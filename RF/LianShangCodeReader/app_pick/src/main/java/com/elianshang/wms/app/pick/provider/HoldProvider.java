package com.elianshang.wms.app.pick.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.pick.bean.PickLocation;
import com.elianshang.wms.app.pick.parser.PickLocationParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 拣货实操接口
 */
public class HoldProvider {

    private static final String base_url = "http://hd01.wms.lsh123.wumart.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/pick/hold";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";


    private static final String taskId = "taskId";


    public static DataHull<PickLocation> request(Context context, String uId, String uToken, String taskId, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(HoldProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(HoldProvider.platform, "2"));
        headers.add(new DefaultKVPBean(HoldProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(HoldProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(HoldProvider.uId, uId));
        headers.add(new DefaultKVPBean(HoldProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(HoldProvider.taskId, taskId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickLocationParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickLocationParser(), 0);

        headers.add(new DefaultKVPBean(HoldProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<PickLocation> handler = new OkHttpHandler();
        DataHull<PickLocation> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
