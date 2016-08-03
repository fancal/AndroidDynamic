package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.ui.BaseActivity;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xfilshy on 16/8/1.
 */
public class ReceiptInfoActivity extends BaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ReceiptInfoActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, String orderOtherId, String containerId, String barCode) {
        Intent intent = new Intent(context, ReceiptInfoActivity.class);
        intent.putExtra("orderOtherId", orderOtherId);
        intent.putExtra("containerId", containerId);
        intent.putExtra("barCode", barCode);
        context.startActivity(intent);

    }

    private String orderOtherId;
    private String containerId;
    private String barCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiptinfo);

        readExtra();

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lotNum" ,"" ) ;
            jsonObject.put("barCode" ,"6925194353326" ) ;
            jsonObject.put("inboundQty" ,1 ) ;
            jsonObject.put("proTime" ,"2016-08-02" ) ;

            jsonArray.put(0 , jsonObject);

//            [{"lotNum" ,""},{"barCode" ,"6925194353326"},{"inboundQty" ,1},{"proTime" ,"2016-08-02"}]
        }catch (Exception e){

        }


        new RequestReceiptAddTask(ReceiptInfoActivity.this, "4536305378", null, "53738630820595", null, jsonArray.toString()).start();
    }

    private void readExtra() {
        orderOtherId = getIntent().getStringExtra("orderOtherId");
        containerId = getIntent().getStringExtra("containerId");
        barCode = getIntent().getStringExtra("barCode");
    }

    private class RequestReceiptAddTask extends HttpAsyncTask<User> {

        /**
         * 物美订单编号
         */
        String orderOtherId = "orderOtherId";

        /**
         * 预约单号
         */
        String bookingNum = "bookingNum";

        /**
         * 托盘码(扫描获得)
         */
        String containerId = "containerId";

        /**
         * 收货码头
         */
        String receiptWharf = "receiptWharf";

        /**
         * 收货明细数组
         */
        String items = "items";

        /**
         * 批次号
         */
        String lotNum = "lotNum";

        /**
         * 商品国条码
         */
        String barCode = "barCode";

        /**
         * 实际收货数
         */
        String inboundQty = "inboundQty";

        /**
         * 生产日期
         */
        String proTime = "proTime";

        public RequestReceiptAddTask(Context context, String orderOtherId, String bookingNum, String containerId, String receiptWharf, String items) {
            super(context, true, true);
            this.orderOtherId = orderOtherId;
            this.bookingNum = bookingNum;
            this.containerId = containerId;
            this.receiptWharf = receiptWharf;
            this.items = items;
        }

        @Override
        public DataHull<User> doInBackground() {
            DataHull<User> dataHull = HttpApi.receiptAdd(orderOtherId, bookingNum, containerId, receiptWharf, items);

            return dataHull;
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            BaseApplication.get().setUser(result);


        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }
}
