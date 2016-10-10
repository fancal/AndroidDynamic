package com.elianshang.wms.app.procurement.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.procurement.bean.ProcurementNext;
import com.elianshang.wms.app.procurement.parser.ProcurementNextParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 移库业务接口
 */
public class ScanLocationProvider {

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

    private static final String _function = "/inhouse/procurement/scanLocation";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    /**
     * 流水号
     */
    private static final String serialNumber = "serialNumber";


    private static final String type = "type";

    /**
     * 任务id
     */
    private static final String taskId = "taskId";

    /**
     * 库位id
     */
    private static final String locationCode = "locationCode";

    /**
     * 操作员id
     */
    private static final String udd = "uId";

    /**
     * 数量
     */
    private static final String uomQty = "uomQty";


    public static DataHull<ProcurementNext> request(Context context, String uId, String uToken, String type, String taskId, String locationCode, String uomQty, String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(ScanLocationProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(ScanLocationProvider.platform, "2"));
        headers.add(new DefaultKVPBean(ScanLocationProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(ScanLocationProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(ScanLocationProvider.uId, uId));
        headers.add(new DefaultKVPBean(ScanLocationProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(ScanLocationProvider.type, type));
        params.add(new DefaultKVPBean(ScanLocationProvider.taskId, taskId));
        params.add(new DefaultKVPBean(ScanLocationProvider.locationCode, locationCode));
        params.add(new DefaultKVPBean(ScanLocationProvider.udd, uId));
        params.add(new DefaultKVPBean(ScanLocationProvider.uomQty, uomQty));
        int hType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<ProcurementNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, hType, new ProcurementNextParser(), 0);

        headers.add(new DefaultKVPBean(ScanLocationProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<ProcurementNext> handler = new OkHttpHandler();
        DataHull<ProcurementNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
