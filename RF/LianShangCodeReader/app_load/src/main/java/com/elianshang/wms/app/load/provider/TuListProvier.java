package com.elianshang.wms.app.load.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.load.bean.TuList;
import com.elianshang.wms.app.load.parser.TuListParser;
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
public class TuListProvier {

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

    private static final String _function = "/outbound/load/getTuList";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String status = "status";

    /**
     *
     * @param context
     * @param uId
     * @param uToken
     * @param status  tu单的状态
     *                1是待装车
     *                5已装车
     *                9发车完毕
     * @return
     */
    public static DataHull<TuList> request(Context context, String uId, String uToken, String status) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TuListProvier.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TuListProvier.platform, "2"));
        headers.add(new DefaultKVPBean(TuListProvier.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TuListProvier.api_version, "v1"));
        headers.add(new DefaultKVPBean(TuListProvier.uId, uId));
        headers.add(new DefaultKVPBean(TuListProvier.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TuListProvier.status, status));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TuListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TuListParser(), 0);

        OkHttpHandler<TuList> handler = new OkHttpHandler();
        DataHull<TuList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
