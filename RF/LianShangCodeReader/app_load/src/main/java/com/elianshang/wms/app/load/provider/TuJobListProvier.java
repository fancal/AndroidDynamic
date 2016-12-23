package com.elianshang.wms.app.load.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.load.bean.TuJobList;
import com.elianshang.wms.app.load.parser.TuJobListParser;
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
public class TuJobListProvier {

<<<<<<< HEAD
    private static final String base_url = "http://static.qatest.rf.lsh123.com/api/wms/rf/v1";
=======
    private static final String base_url = "http://static.qatest2.rf.lsh123.com/api/wms/rf/v1";
>>>>>>> RF_1.0_QATEST2

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

    private static final String _function = "/outbound/load/getTuJob";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String tuId = "tuId";

    public static DataHull<TuJobList> request(Context context, String uId, String uToken, String tuId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(TuJobListProvier.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(TuJobListProvier.platform, "2"));
        headers.add(new DefaultKVPBean(TuJobListProvier.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(TuJobListProvier.api_version, "v1"));
        headers.add(new DefaultKVPBean(TuJobListProvier.uId, uId));
        headers.add(new DefaultKVPBean(TuJobListProvier.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(TuJobListProvier.tuId, tuId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<TuJobListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new TuJobListParser(), 0);

        OkHttpHandler<TuJobList> handler = new OkHttpHandler();
        DataHull<TuJobList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
