package com.elianshang.wms.app.create_scrap.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.app.create_scrap.bean.Item;
import com.elianshang.wms.app.create_scrap.parser.ItemParser;
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
public class StockItemProvider {

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

    private static final String _function = "/inhouse/stock/getItem";

    /**
     * 位置ID
     */
    private static final String locationId = "locationId";

    private static final String barCode = "barcode";


    public static DataHull<Item> request(String locationId, String barCode) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(StockItemProvider.app_key, ""));
        headers.add(new DefaultKVPBean(StockItemProvider.platform, ""));
        headers.add(new DefaultKVPBean(StockItemProvider.version, ""));
        headers.add(new DefaultKVPBean(StockItemProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(StockItemProvider.locationId, locationId));
        params.add(new DefaultKVPBean(StockItemProvider.barCode, barCode));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ItemParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new ItemParser(), 0);

        OkHttpHandler<Item> handler = new OkHttpHandler();
        DataHull<Item> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
