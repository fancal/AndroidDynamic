package com.elianshang.wms.app.back.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.back.bean.BackList;
import com.elianshang.wms.app.back.parser.BackListParser;
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
public class ScanProvider {

    private static final String base_url = "http://hd01.rf.wms.lsh123.wumart.com/api/wms/rf/v1";

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

    private static final String _function = "deng";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String codeList = "codeList";

    public static DataHull<BackList> request(Context context, String uId, String uToken, String codeList) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanProvider.codeList, codeList));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<BackListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new BackListParser(), 0);

        OkHttpHandler<BackList> handler = new OkHttpHandler();
        DataHull<BackList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
