package com.elianshang.wms.app.qc.provider;

import android.content.Context;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.MD5Tool;
import com.elianshang.wms.app.qc.bean.QCDoneState;
import com.elianshang.wms.app.qc.parser.QCDoneParser;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;

import java.util.ArrayList;
import java.util.List;

public class QCOneItemProvider {

    private static final String base_url = "http://hd01.wms.lsh123.wumart.com/api/wms/rf/v1";

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

    private static final String _function = "/outbound/qc/qcOneItem";

    private static final String uId = "uid";

    private static final String uToken = "utoken";

    private static final String serialNumber = "serialNumber";

    private static final String qcTaskId = "qcTaskId";

    private static final String code = "code";

    private static final String uomQty = "uomQty";

    private static final String defectQty = "defectQty";

    public static DataHull<QCDoneState> request(Context context, String uId, String uToken, String qcTaskId, String code, String uomQty, String defectQty , String serialNumber) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(QCOneItemProvider.app_key, DeviceTool.getIMEI(context)));
        headers.add(new DefaultKVPBean(QCOneItemProvider.platform, "2"));
        headers.add(new DefaultKVPBean(QCOneItemProvider.version, DeviceTool.getClientVersionName(context)));
        headers.add(new DefaultKVPBean(QCOneItemProvider.api_version, "v1"));
        headers.add(new DefaultKVPBean(QCOneItemProvider.uId, uId));
        headers.add(new DefaultKVPBean(QCOneItemProvider.uToken, uToken));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(QCOneItemProvider.qcTaskId, qcTaskId));
        params.add(new DefaultKVPBean(QCOneItemProvider.code, code));
        params.add(new DefaultKVPBean(QCOneItemProvider.uomQty, uomQty));
        params.add(new DefaultKVPBean(QCOneItemProvider.defectQty, defectQty));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<QCDoneParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new QCDoneParser(), 0);

        headers.add(new DefaultKVPBean(QCOneItemProvider.serialNumber, MD5Tool.toMd5(serialNumber + parameter.encodeUrl())));

        OkHttpHandler<QCDoneState> handler = new OkHttpHandler();
        DataHull<QCDoneState> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
