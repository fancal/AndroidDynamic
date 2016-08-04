package com.elianshang.code.reader.asyn;

import android.content.Context;

import com.elianshang.code.reader.bean.ProductList;
import com.elianshang.code.reader.http.HttpApi;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 库位商品列表
 */
public class LocationProductListTask extends HttpAsyncTask<ProductList> {

    private String locationId;

    public CallBack callBack;

    public LocationProductListTask(Context context, String locationId, CallBack callBack) {
        super(context, true, true, false);
        this.locationId = locationId;
        this.callBack = callBack;
    }

    @Override
    public DataHull<ProductList> doInBackground() {
        return HttpApi.stockGetItemList(locationId);
    }

    @Override
    public void onPostExecute(int updateId, ProductList result) {
        if (callBack != null) {
            callBack.success(result);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        super.netErr(updateId, errMsg);
        if (callBack != null) {
            callBack.failed(errMsg);
        }
    }

    @Override
    public void netNull() {
        super.netNull();
        if (callBack != null) {
            callBack.failed("网络异常");
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        super.dataNull(updateId, errMsg);
        if (callBack != null) {
            callBack.failed(errMsg);
        }
    }

    public interface CallBack {
        void success(ProductList products);

        void failed(String errStr);
    }
}
