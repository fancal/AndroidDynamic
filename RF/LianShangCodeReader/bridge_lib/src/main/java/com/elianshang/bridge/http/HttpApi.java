package com.elianshang.bridge.http;

import com.elianshang.bridge.parser.MasterParser;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;
import com.xue.http.parse.BaseParser;

import java.util.ArrayList;
import java.util.List;


public class HttpApi {

    private static String base_url;

    private static String api_version;

    private static String version;

    private static DefaultKVPBean[] dynamicHeader;
    /**
     * 公钥
     */
    private static String secretKey = null;

    private static List<BaseKVP> default_headers = null;

    private interface Header {

        /**
         * 用户ID
         */
        String uid = "uid";

        /**
         * 用户token
         */
        String token = "utoken";

        /**
         * app唯一标示传imei
         */
        String app_key = "app-key";

        /**
         * 平台(1.H5  2.Android)
         */
        String platform = "platform";

        /**
         * app版本
         */
        String version = "app-version";

        /**
         * api版本
         */
        String api_version = "api-version";

    }

    /**
     * 公有参数
     */
    private interface PublicParameter {

        String timestampt = "timestamp";
    }


    public static void build(String baseUrl, String apiVersion, String version, DefaultKVPBean... dynamicHeader) {
        HttpApi.base_url = baseUrl;
        HttpApi.api_version = apiVersion;
        HttpApi.version = version;
        HttpApi.dynamicHeader = dynamicHeader;
        OkHttpHandler.setOpensslSecret(null);
//        OkHttpHandler.setOpensslSecret(SecretTool.getOpensslSecretKey(BaseApplication.get()));
    }


    private synchronized static void setDefaultHeaders(BaseKVP... kvPairs) {
        if (default_headers == null) {
            default_headers = new ArrayList<>();
            for (int i = 0; i < kvPairs.length; i++) {
                default_headers.add(kvPairs[i]);
            }
        }
    }


    private static void setDefaultHeaders() {
        setDefaultHeaders(
                new DefaultKVPBean(Header.api_version, api_version),
                new DefaultKVPBean(Header.version, version),
                //new DefaultKVPBean(Header.app_key, BaseApplication.get().getImei()),
                new DefaultKVPBean(Header.app_key, "11111"),
                new DefaultKVPBean(Header.platform, "2")
        );
    }


    /**
     * 获取默认Header
     *
     * @return
     */
    private static List<BaseKVP> getDefaultHeaders() {
        if (default_headers == null) {
            setDefaultHeaders();
        }

        List<BaseKVP> headerList = new ArrayList();
        headerList.addAll(default_headers);
        if (dynamicHeader != null) {
            addHeaders(headerList, dynamicHeader);
        }
        return headerList;
    }

    /**
     * 添加Header
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(BaseKVP... kvPairs) {
        List<BaseKVP> headers = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }


    /**
     * 添加Header
     *
     * @param headers
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(List<BaseKVP> headers, BaseKVP... kvPairs) {
        if (headers == null) {
            headers = addHeaders(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }


    /**
     * 添加请求参数
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(BaseKVP... kvPairs) {
        List<BaseKVP> params = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }


    /**
     * 添加请求参数
     *
     * @param params
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(List<BaseKVP> params, BaseKVP... kvPairs) {
        if (params == null) {
            params = addParams(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }

    /**
     * 根据参数，调起请求
     */
    public static <B extends BaseBean, D, PR extends BaseParser<B, D>> DataHull<B> request(HttpDynamicParameter<PR> httpParameter) {
//        httpParameter.addParameter(new DefaultKVPBean(PublicParameter.timestampt, String.valueOf(TimestampTool.currentTimeMillis() / 1000)));
        OkHttpHandler<B> handler = new OkHttpHandler<>();
        DataHull<B> dataHull = handler.requestData(httpParameter);
        return dataHull;
    }

    /**
     * get请求
     *
     * @param function
     * @param parser
     * @param kvpBeen
     * @param <B>
     * @param <M>
     * @return
     */
    public static <B extends BaseBean, M extends MasterParser<B>> DataHull<B> doGet(String function, M parser, DefaultKVPBean... kvpBeen) {
        String url = base_url + function;
        List<BaseKVP> params = addParams(kvpBeen);
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, parser, 0);

        return request(parameter);
    }

    /**
     * get请求
     *
     * @param function
     * @param parser
     * @param kvpBeen
     * @param <B>
     * @param <M>
     * @return
     */
    public static <B extends BaseBean, M extends MasterParser<B>> DataHull<B> doPost(String function, M parser, DefaultKVPBean... kvpBeen) {
        String url = base_url + function;
        List<BaseKVP> params = addParams(kvpBeen);
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, parser, 0);

        return request(parameter);
    }


}
