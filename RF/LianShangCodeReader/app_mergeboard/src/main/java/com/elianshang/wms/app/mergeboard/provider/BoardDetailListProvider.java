package com.elianshang.wms.app.mergeboard.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.mergeboard.bean.BoardDetailList;
import com.elianshang.wms.app.mergeboard.parser.BoardDetailListParser;
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
public class BoardDetailListProvider {

    private static final String base_url = "http://rf.wmdev.lsh123.com/api/wms/rf/v1";

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

    private static final String _function = "/inhouse/stock_taking/doOne";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String result = "result";


    public static DataHull<BoardDetailList> request(Context context, String uId, String uToken, String result) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(BoardDetailListProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(BoardDetailListProvider.platform, "2"));
        headers.add(new DefaultKVPBean(BoardDetailListProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(BoardDetailListProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(BoardDetailListProvider.uId, uId));
        headers.add(new DefaultKVPBean(BoardDetailListProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(BoardDetailListProvider.result, result));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<BoardDetailListParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new BoardDetailListParser(), 0);

        OkHttpHandler<BoardDetailList> handler = new OkHttpHandler();
        DataHull<BoardDetailList> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
