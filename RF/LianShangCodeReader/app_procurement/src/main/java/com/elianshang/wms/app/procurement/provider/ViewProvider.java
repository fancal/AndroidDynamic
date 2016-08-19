package com.elianshang.wms.app.procurement.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.procurement.bean.TaskTransferDetail;
import com.elianshang.wms.app.procurement.parser.TaskTransferDetailParser;
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
public class ViewProvider {

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

    private static final String _function = "/inhouse/procurement/view";

    /**
     * 任务ID
     */
    private static final String taskId = "taskId";


    public static DataHull<TaskTransferDetail> request(String taskId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ViewProvider.app_key, ""));
        headers.add(new DefaultKVPBean(ViewProvider.platform, ""));
        headers.add(new DefaultKVPBean(ViewProvider.version, ""));
        headers.add(new DefaultKVPBean(ViewProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ViewProvider.taskId, taskId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TaskTransferDetailParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TaskTransferDetailParser(), 0);

        OkHttpHandler<TaskTransferDetail> handler = new OkHttpHandler();
        DataHull<TaskTransferDetail> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
