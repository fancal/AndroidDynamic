package com.elianshang.wms.app.setofgoods.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.setofgoods.bean.SetOfGoodsView;
import com.elianshang.wms.app.setofgoods.parser.SetOfGoodsViewParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class SetOfGoodsViewProvider {

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

    private static final String _function = "/setGoods/view";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String containerId = "containerId";

    public static DataHull<SetOfGoodsView> request(Context context, String uId, String uToken, String containerId) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.platform, "2"));
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.uId, uId));
        headers.add(new DefaultKVPBean(SetOfGoodsViewProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(SetOfGoodsViewProvider.containerId, containerId));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<SetOfGoodsViewParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new SetOfGoodsViewParser(), 0);

        OkHttpHandler<SetOfGoodsView> handler = new OkHttpHandler();
        DataHull<SetOfGoodsView> dataHull = handler.requestData(parameter);
        return dataHull;
    }
}
