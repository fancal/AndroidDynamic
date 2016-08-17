package com.elianshang.bridge.http;

import com.xue.http.hook.BaseKVP;

import java.util.List;

/**
 * Created by liuhanzhi on 16/8/17.
 */
public interface HttpDynamicHeader {

    List<BaseKVP> getDynamicHeader();

}
