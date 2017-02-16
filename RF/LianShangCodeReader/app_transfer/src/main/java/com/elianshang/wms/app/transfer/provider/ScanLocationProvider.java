package com.elianshang.wms.app.transfer.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.transfer.bean.TransferNext;
import com.elianshang.wms.app.transfer.parser.StockTransferNextParser;
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
public class ScanLocationProvider {

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

    private static final String _function = "/inhouse/transfer/scanLocation";

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
     * 数量
     */
    private static final String uomQty = "uomQty";

    private static final String barcode = "barcode";

    private static final String uom = "uom";

    private static final String subType = "subType";


    public static DataHull<TransferNext> request(Context context, String uId, String uToken, String type, String taskId, String locationCode, String barCode, String uom, String uomQty, String subType, String serialNumber) {
        String url = HostTool.curHost.getHostUrl() + _function;

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
        params.add(new DefaultKVPBean(ScanLocationProvider.barcode, barCode));
        params.add(new DefaultKVPBean(ScanLocationProvider.uom, uom));
        params.add(new DefaultKVPBean(ScanLocationProvider.uomQty, uomQty));
        params.add(new DefaultKVPBean(ScanLocationProvider.subType, subType));
        int hType = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<StockTransferNextParser> parameter = new HttpDynamicParameter<>(url, headers, params, hType, new StockTransferNextParser(), 0);

        headers.add(new DefaultKVPBean(ScanLocationProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<TransferNext> handler = new OkHttpHandler();
        DataHull<TransferNext> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
