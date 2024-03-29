package com.xue.http.impl;

import android.text.TextUtils;

import com.xue.http.HttpLogTool;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseKVP;
import com.xue.http.okhttp.OkHttpParameter;
import com.xue.http.parse.BaseParser;

import java.net.URLEncoder;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 动态请求参数类
 */
public abstract class OkHttpDynamicParameter<PR extends BaseParser<? extends BaseBean, ?>> extends OkHttpParameter<PR> {

    public OkHttpDynamicParameter(String baseUrl, List<BaseKVP> headers, List<BaseKVP> params, int type, PR parser, int updataId, String secretKey) {
        super(baseUrl, headers, params, type, parser, updataId, secretKey);
    }

    @Override
    public String buildParameter(Request.Builder requestBuilder) {
        if (requestBuilder != null) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            if (getParams() != null && getParams().size() > 0) {
                HttpLogTool.log("--------------Parameters--------------");
                for (BaseKVP baseKVP : getParams()) {
                    if (baseKVP != null && !TextUtils.isEmpty(baseKVP.getKey()) && !TextUtils.isEmpty(baseKVP.getValue())) {
                        formBuilder.add(baseKVP.getKey(), baseKVP.getValue());
                        HttpLogTool.log(baseKVP.getKey() + ": " + baseKVP.getValue());
                    }
                }
            }

            RequestBody requestBody = formBuilder.build();
            requestBuilder.post(requestBody);

            return null;
        } else {
            return encodeUrl().toString();
        }
    }

    public StringBuilder encodeUrl() {
        StringBuilder sb = new StringBuilder();
        if (getParams() == null) {
            return sb;
        }
        boolean first = true;

        for (BaseKVP kvpBean : getParams()) {
            if (!TextUtils.isEmpty(kvpBean.getKey()) && !TextUtils.isEmpty(kvpBean.getValue())) {
                if (first) {
                    if (getType() == Type.GET) {
                        sb.append("?");
                    }
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(kvpBean.getKey() + "=" + URLEncoder.encode(kvpBean.getValue()));
            }
        }
        return sb;
    }
}