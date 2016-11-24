package com.elianshang.wms.app.pick.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.pick.bean.Split;
import com.elianshang.wms.app.pick.parser.SplitParser;
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
public class SplitNewPickTaskProvider {

    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/pick/splitNewPickTask";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";

    private static final String taskId = "taskId";

    private static final String containerId = "containerId";

    public static DataHull<Split> request(Context context, String uId, String uToken, String taskId, String containerId , String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.platform, "2"));
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(SplitNewPickTaskProvider.taskId, taskId));
        params.add(new DefaultKVPBean(SplitNewPickTaskProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SplitParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SplitParser(), 0);

        headers.add(new DefaultKVPBean(SplitNewPickTaskProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<Split> handler = new OkHttpHandler();
        DataHull<Split> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
