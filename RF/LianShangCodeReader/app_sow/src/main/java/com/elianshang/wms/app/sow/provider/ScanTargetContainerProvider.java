package com.elianshang.wms.app.sow.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.sow.bean.SowNext;
import com.elianshang.wms.app.sow.parser.SowNextParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ScanTargetContainerProvider {

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

    private static final String _function = "/inbound/pick_up_shelve/scanTargetLocation";

    private static final String uId = "uId";

    private static final String uToken = "utoken";

    /**
     * 流水号
     */
    private static final String serialNumber = "serialNumber";

    /**
     * 任务ID
     */
    private static final String taskId = "taskId";

    private static final String allocContainerId = "allocContainerId";

    private static final String realContainerId = "realContainerId";

    private static final String qty = "qty";


    public static DataHull<SowNext> request(Context context, String uid, String uToken, String taskId, String allocContainerId, String realContainerId, String qty, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.uId, uid));
        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanTargetContainerProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanTargetContainerProvider.allocContainerId, allocContainerId));
        params.add(new DefaultKVPBean(ScanTargetContainerProvider.realContainerId, realContainerId));
        params.add(new DefaultKVPBean(ScanTargetContainerProvider.qty, qty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SowNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SowNextParser(), 0);

        headers.add(new DefaultKVPBean(ScanTargetContainerProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<SowNext> handler = new OkHttpHandler();
        DataHull<SowNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}