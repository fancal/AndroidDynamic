package com.elianshang.wms.app.capacity.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.capacity.bean.MergeBean;
import com.elianshang.wms.app.capacity.parser.MergeBeanParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class MergeProvider {

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

    private static final String _function = "inbound/location/mergeBins";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String binCodes = "binCodes";

    private static final String serialNumber = "serialNumber";


    public static DataHull<MergeBean> request(Context context, String uId, String uToken, String binCodes, String serialNumber) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(MergeProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(MergeProvider.platform, "2"));
        headers.add(new DefaultKVPBean(MergeProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(MergeProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(MergeProvider.uId, uId));
        headers.add(new DefaultKVPBean(MergeProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(MergeProvider.binCodes, binCodes));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<MergeBeanParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new MergeBeanParser(), 0);
        headers.add(new DefaultKVPBean(MergeProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));
        OkHttpHandler<MergeBean> handler = new OkHttpHandler();
        DataHull<MergeBean> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
