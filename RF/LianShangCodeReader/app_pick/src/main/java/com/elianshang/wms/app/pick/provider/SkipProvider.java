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
public class SkipProvider {

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

    private static final String _function = "/outbound/pick/skip";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";


    private static final String taskId = "taskId";

    private static final String pickOrder = "pickOrder";


    public static DataHull<PickLocation> request(Context context, String uId, String uToken, String taskId, String pickOrder, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(SkipProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(SkipProvider.platform, "2"));
        headers.add(new DefaultKVPBean(SkipProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(SkipProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(SkipProvider.uId, uId));
        headers.add(new DefaultKVPBean(SkipProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(SkipProvider.taskId, taskId));
        params.add(new DefaultKVPBean(SkipProvider.pickOrder, pickOrder));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickLocationParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickLocationParser(), 0);

        headers.add(new DefaultKVPBean(SkipProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<PickLocation> handler = new OkHttpHandler();
        DataHull<PickLocation> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
