package com.elianshang.wms.app.pickup.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.pickup.bean.PickUpView;
import com.elianshang.wms.app.pickup.parser.PickUpViewParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class PickUpViewProvider {

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

    private static final String _function = "/setGoods/view";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String containerId = "containerId";

    public static DataHull<PickUpView> request(Context context, String uId, String uToken, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(PickUpViewProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(PickUpViewProvider.platform, "2"));
        headers.add(new DefaultKVPBean(PickUpViewProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(PickUpViewProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(PickUpViewProvider.uId, uId));
        headers.add(new DefaultKVPBean(PickUpViewProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(PickUpViewProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<PickUpViewParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new PickUpViewParser(), 0);

        OkHttpHandler<PickUpView> handler = new OkHttpHandler();
        DataHull<PickUpView> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
