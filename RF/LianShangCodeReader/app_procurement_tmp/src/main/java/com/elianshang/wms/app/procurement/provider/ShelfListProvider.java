package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.procurement.bean.ShelfList;
import com.elianshang.wms.app.procurement.parser.ShelfListParser;
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
public class ShelfListProvider {

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

    private static final String _function = "/inhouse/procurement/fetchTask";

    private static final String uId = "uid";

    private static final String uToken = "utoken";



    public static DataHull<ShelfList> request(Context context, String uId, String uToken) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ShelfListProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ShelfListProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ShelfListProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ShelfListProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ShelfListProvider.uId, uId));
        headers.add(new DefaultKVPBean(ShelfListProvider.uToken, uToken));

        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ShelfListParser> parameter = new HttpDynamicParameter<>(url, headers, null, type, new ShelfListParser(), 0);

        OkHttpHandler<ShelfList> handler = new OkHttpHandler();
        DataHull<ShelfList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
