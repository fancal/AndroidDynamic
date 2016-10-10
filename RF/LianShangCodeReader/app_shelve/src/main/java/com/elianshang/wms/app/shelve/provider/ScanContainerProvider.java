package com.elianshang.wms.app.shelve.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.shelve.bean.Shelve;
import com.elianshang.wms.app.shelve.parser.ShelveParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 领取任务接口
 */
public class ScanContainerProvider {

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

    private static final String _function = "/inbound/shelve/scanContainer";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 托盘码
     */
    private static final String containerId = "containerId";


    public static DataHull<Shelve> request(Context context, String uId, String uToken, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanContainerProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanContainerProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ShelveParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ShelveParser(), 0);

        OkHttpHandler<Shelve> handler = new OkHttpHandler();
        DataHull<Shelve> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
