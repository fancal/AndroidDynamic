package com.elianshang.wms.app.sow.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.sow.bean.StoreList;
import com.elianshang.wms.app.sow.parser.StoreListParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ScanContainerProvider {

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

    private static final String _function = "/seed/scanContainer";

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

    private static final String containerId = "containerId";

    private static final String qty = "qty";

    private static final String scatterQty = "scatterQty";

    private static final String type = "type";

    private static final String storeNo = "storeNo";

    private static final String exceptionCode = "exceptionCode";


    public static DataHull<StoreList> request(Context context, String uid, String uToken, String taskId, String containerId, String qty, String scatterQty, String scanType, String storeNo, String exceptionCode, String serialNumber) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanContainerProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanContainerProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uId, uid));
        headers.add(new DefaultKVPBean(ScanContainerProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanContainerProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanContainerProvider.containerId, containerId));
        params.add(new DefaultKVPBean(ScanContainerProvider.qty, qty));
        params.add(new DefaultKVPBean(ScanContainerProvider.scatterQty, scatterQty));
        params.add(new DefaultKVPBean(ScanContainerProvider.type, scanType));
        params.add(new DefaultKVPBean(ScanContainerProvider.storeNo, storeNo));
        params.add(new DefaultKVPBean(ScanContainerProvider.exceptionCode, exceptionCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StoreListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new StoreListParser(), 0);

        headers.add(new DefaultKVPBean(ScanContainerProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<StoreList> handler = new OkHttpHandler();
        DataHull<StoreList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
