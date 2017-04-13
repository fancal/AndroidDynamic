package com.elianshang.wms.app.transfer.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.parser.StockTransferParser;
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
public class RestoreTaskProvider {

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

    private static final String _function = "/inhouse/transfer/restore";


    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 操作员id
     */
    private static final String udd = "uId";


    public static DataHull<Transfer> request(Context context, String uId, String uToken) {
        String url = HostTool.curHost.getHostUrl() + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(RestoreTaskProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(RestoreTaskProvider.platform, "2"));
        headers.add(new DefaultKVPBean(RestoreTaskProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(RestoreTaskProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(RestoreTaskProvider.uId, uId));
        headers.add(new DefaultKVPBean(RestoreTaskProvider.uToken, uToken));

        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StockTransferParser> parameter = new HttpDynamicParameter<>(url, headers, null, type, new StockTransferParser(), 0);

        OkHttpHandler<Transfer> handler = new OkHttpHandler();
        DataHull<Transfer> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}