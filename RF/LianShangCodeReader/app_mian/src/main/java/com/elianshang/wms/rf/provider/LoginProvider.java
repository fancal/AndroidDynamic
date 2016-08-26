package com.elianshang.wms.rf.provider;

import com.elianshang.bridge.http.HttpDynamicParameter;
import com.elianshang.wms.rf.bean.User;
import com.elianshang.wms.rf.parser.UserParser;
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
public class LoginProvider {

    private static final String base_url = "http://rf.wmdev.lsh123.com/api/wms/rf/v1";

    private static final String _function = "/user/login";

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

    /**
     * 用户名
     */
    private static final String userName = "userName";

    /**
     * 密码
     */
    private static final String passwd = "passwd";


    public static DataHull<User> request(String userName, String passwd) {
        String url = base_url + _function;

        List<BaseKVP> headers = new ArrayList<>();
        headers.add(new DefaultKVPBean(LoginProvider.app_key, ""));
        headers.add(new DefaultKVPBean(LoginProvider.platform, ""));
        headers.add(new DefaultKVPBean(LoginProvider.version, ""));
        headers.add(new DefaultKVPBean(LoginProvider.api_version, ""));

        List<BaseKVP> params = new ArrayList<>();
        params.add(new DefaultKVPBean(LoginProvider.userName, userName));
        params.add(new DefaultKVPBean(LoginProvider.passwd, passwd));
        int type = BaseHttpParameter.Type.POST;

        HttpDynamicParameter<UserParser> parameter = new HttpDynamicParameter<>(url, headers, params, type, new UserParser(), 0);

        OkHttpHandler<User> handler = new OkHttpHandler();
        DataHull<User> dataHull = handler.requestData(parameter);
        return dataHull;

    }
}
