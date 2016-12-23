package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.parser.ProcurementParser;
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
public class AssignTaskProvider {

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

    private static final String _function = "/inhouse/procurement/assign";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String taskId = "taskId";


    public static DataHull<Procurement> request(Context context, String uId, String uToken, String taskId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(AssignTaskProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(AssignTaskProvider.platform, "2"));
        headers.add(new DefaultKVPBean(AssignTaskProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(AssignTaskProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(AssignTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(AssignTaskProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(AssignTaskProvider.taskId, taskId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ProcurementParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ProcurementParser(), 0);

        OkHttpHandler<Procurement> handler = new OkHttpHandler();
        DataHull<Procurement> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
