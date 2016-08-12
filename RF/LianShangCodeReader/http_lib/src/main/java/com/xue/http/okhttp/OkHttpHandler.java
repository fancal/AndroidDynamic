package com.xue.http.okhttp;

import com.xue.http.HttpLogTool;
import com.xue.http.exception.ResponseException;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.HttpHandler;
import com.xue.http.parse.BaseParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求处理类，okhttp实现
 */

public final class OkHttpHandler<B extends BaseBean> extends HttpHandler<OkHttpParameter<? extends BaseParser<B, ?>>, B> {

    private int code;

    private static String opensslSecret;

    public static void setOpensslSecret(String opensslSecret) {
        OkHttpHandler.opensslSecret = opensslSecret;
    }

    @Override
    public String doGet(OkHttpParameter params) throws ResponseException, IOException {

        OkHttpClient okHttpClient = OkHttpClientBuidler.get(opensslSecret).getOkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        String baseUrl = params.getBaseUrl() + params.buildParameter(null);
        HttpLogTool.log("GET  " + baseUrl);
        requestBuilder.url(baseUrl);
        params.buildHeader(requestBuilder);

        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            response.body().close();
            return data;
        }
        if (response.body() != null) {
            String data = response.body().string();
            HttpLogTool.log("code:" + response.code() + " ,data:" + data);
            response.body().close();
        }
        code = response.code();

        throw new ResponseException();
    }

    @Override
    public String doPost(OkHttpParameter params) throws ResponseException, IOException {

        OkHttpClient okHttpClient = OkHttpClientBuidler.get(opensslSecret).getOkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        String baseUrl = params.getBaseUrl();
        HttpLogTool.log("POST  " + baseUrl);
        requestBuilder.url(baseUrl);
        params.buildHeader(requestBuilder);
        params.buildParameter(requestBuilder);
        //return "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20160810190219\"},\"body\":{\"type\":1,\"qcList\":[{\"itemName\":\"aa1\",\"code\":\"131123131\",\"pickQty\":\"4\",\"packName\":\"H24\"},{\"itemName\":\"aa1\",\"code\":\"1312355131\",\"pickQty\":\"4\",\"packName\":\"H24\"},{\"itemName\":\"aa1\",\"code\":\"1312443131\",\"pickQty\":\"4\",\"packName\":\"H24\"},{\"itemName\":\"aa1\",\"code\":\"1312333131\",\"pickQty\":\"4\",\"packName\":\"H24\"},{\"itemName\":\"aa1\",\"code\":\"1312312231\",\"pickQty\":\"4\",\"packName\":\"H24\"}]}}";
        return "{\"head\":{\"status\":1,\"message\":\"success\",\"timestamp\":\"20160812163245\"},\"body\":{\"fromLocationId\":100,\"fromLocationCode\":\"fromLocationCode\",\"itemId\":\"itemId\",\"itemName\":\"itemName\",\"packName\":\"packName\",\"toLocationId\":10,\"toLocationCode\":\"toLocationCode\",\"uomQty\":3}}";

//        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
//        if (response.isSuccessful()) {
//            String data = response.body().string();
//            response.body().close();
//            return data;
//        } else {
//            if (response.body() != null) {
//                String data = response.body().string();
//                HttpLogTool.log("code:" + response.code() + " ,data:" + data);
//                response.body().close();
//            }
//        }
//
//        code = response.code();
//
//        throw new ResponseException();
    }

    @Override
    public int getResponseCode() {
        return code;
    }
}
