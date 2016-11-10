package com.elianshang.wms.app.mergeboard.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.mergeboard.bean.ResponseState;
import com.elianshang.wms.app.mergeboard.parser.ResponseStateParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class MergeContainersProvider {

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

    private static final String _function = "/outbound/merge/mergeContainers";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";

    private static final String containerIds = "containerIds";

    private static final String taskBoardQty = "taskBoardQty";


    public static DataHull<ResponseState> request(Context context, String uId, String uToken, String containerIds, String taskBoardQty ,String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(MergeContainersProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(MergeContainersProvider.platform, "2"));
        headers.add(new DefaultKVPBean(MergeContainersProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(MergeContainersProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(MergeContainersProvider.uId, uId));
        headers.add(new DefaultKVPBean(MergeContainersProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(MergeContainersProvider.containerIds, containerIds));
        params.add(new DefaultKVPBean(MergeContainersProvider.taskBoardQty, taskBoardQty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ResponseStateParser(), 0);
        headers.add(new DefaultKVPBean(MergeContainersProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));
        OkHttpHandler<ResponseState> handler = new OkHttpHandler();
        DataHull<ResponseState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
