package com.elianshang.wms.rf.atticshelve.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.rf.atticshelve.bean.AtticShelveNext;
import com.elianshang.wms.rf.atticshelve.parser.AtticShelveNextParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class ScanTargetLocationProvider {

    private static final String base_url = "http://hz01.rf.wms.lsh123.com/api/wms/rf/v1";

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

    private static final String allocLocationCode = "allocLocationCode";

    private static final String realLocationCode = "realLocationCode";

    private static final String qty = "qty";


    public static DataHull<AtticShelveNext> request(Context context, String uid, String uToken, String taskId, String allocLocationCode, String realLocationCode, String qty, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.uId, uid));
        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.allocLocationCode, allocLocationCode));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.realLocationCode, realLocationCode));
        params.add(new DefaultKVPBean(ScanTargetLocationProvider.qty, qty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<AtticShelveNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new AtticShelveNextParser(), 0);

        headers.add(new DefaultKVPBean(ScanTargetLocationProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<AtticShelveNext> handler = new OkHttpHandler();
        DataHull<AtticShelveNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
