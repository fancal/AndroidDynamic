package com.elianshang.wms.app.create_return.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.create_return.bean.Item;
import com.elianshang.wms.app.create_return.parser.ItemParser;
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
public class GetItemProvider {

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

    private static final String _function = "/inhouse/stock/getItem";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 位置ID
     */
    private static final String locationCode = "locationCode";

    private static final String barCode = "barcode";


    public static DataHull<Item> request(Context context, String uId, String uToken, String locationCode, String barCode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(GetItemProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(GetItemProvider.platform, "2"));
        headers.add(new DefaultKVPBean(GetItemProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(GetItemProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(GetItemProvider.uId, uId));
        headers.add(new DefaultKVPBean(GetItemProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(GetItemProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(GetItemProvider.barCode, barCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ItemParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ItemParser(), 0);

        OkHttpHandler<Item> handler = new OkHttpHandler();
        DataHull<Item> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}