package com.elianshang.wms.app.load.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.load.bean.ContainerInfo;
import com.elianshang.wms.app.load.parser.ContainerInfoParser;
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
public class ContainerInfoProvier {

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

    private static final String _function = "/outbound/load/showContainer";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String containerId = "containerId";

    private static final String tuId = "tuId";

    public static DataHull<ContainerInfo> request(Context context, String uId, String uToken, String tuId, String containerId) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ContainerInfoProvier.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ContainerInfoProvier.platform, "2"));
        headers.add(new DefaultKVPBean(ContainerInfoProvier.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ContainerInfoProvier.api_version, "v1"));
        headers.add(new DefaultKVPBean(ContainerInfoProvier.uId, uId));
        headers.add(new DefaultKVPBean(ContainerInfoProvier.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ContainerInfoProvier.containerId, containerId));
        params.add(new DefaultKVPBean(ContainerInfoProvier.tuId, tuId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ContainerInfoParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ContainerInfoParser(), 0);

        OkHttpHandler<ContainerInfo> handler = new OkHttpHandler();
        DataHull<ContainerInfo> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
