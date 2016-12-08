package com.elianshang.wms.app.receipt.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.receipt.R;
import com.elianshang.wms.app.receipt.bean.OrderList;
import com.elianshang.wms.app.receipt.bean.StoreReceiptInfo;
import com.elianshang.wms.app.receipt.provider.StoreInfoProvider;
import com.elianshang.wms.app.receipt.provider.StoreOrderListProvider;
import com.xue.http.impl.DataHull;

/**
 * 收货扫描页,扫描 超市id,托盘码,商品barcode
 */
public class StoreOpenActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), StoreOpenActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    /**
     * 门店编码输入框
     */
    private ScanEditText storeIdEditText;

    /**
     * 托盘码扫描输入框
     */
    private ScanEditText containerIdEditText;

    /**
     * 国条码扫描输入框
     */
    private ScanEditText barCodeEditText;

    /**
     * 提交按钮
     */
    private Button submitButton;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 输入框工具
     */
    private ScanEditTextTool scanEditTextTool;

    private View scanLayout;

    private ListView orderListView;

    private OrderList orderList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity_storeopen);

        if (readExtras()) {
            findViews();
            fillScan();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ScanManager.get() != null) {
            ScanManager.get().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ScanManager.get() != null) {
            ScanManager.get().removeListener(this);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fillScan();
            barCodeEditText.requestFocus();
        }
    }

    private void findViews() {
        storeIdEditText = (ScanEditText) findViewById(R.id.storeId_EditText);
        containerIdEditText = (ScanEditText) findViewById(R.id.containerId_EditText);
        barCodeEditText = (ScanEditText) findViewById(R.id.barCode_EditText);
        submitButton = (Button) findViewById(R.id.submit_Button);

        scanLayout = findViewById(R.id.scan_Layout);
        orderListView = (ListView) findViewById(R.id.order_ListView);
        submitButton.setEnabled(false);
        scanEditTextTool = new ScanEditTextTool(that, storeIdEditText, containerIdEditText, barCodeEditText);
        scanEditTextTool.setComplete(this);

        submitButton.setOnClickListener(this);

        orderListView.setOnItemClickListener(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressBack();
            }
        });
    }

    private void fillScan() {
        scanLayout.setVisibility(View.VISIBLE);
        orderListView.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        orderListView.setAdapter(null);
    }

    private void fillList() {
        if (orderList != null) {
            OrderListAdapter adapter = new OrderListAdapter();
            orderListView.setAdapter(adapter);
        }

        scanLayout.setVisibility(View.GONE);
        orderListView.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
    }

    private void submit() {
        String storeId = storeIdEditText.getText().toString().trim();
        String containerId = containerIdEditText.getText().toString().trim();
        String barCode = barCodeEditText.getText().toString().trim();
        new RequestOrderListTask(that, storeId, containerId, barCode).start();
    }

    private void pressBack() {
        String storeId = storeIdEditText.getText().toString().trim();
        String containerId = containerIdEditText.getText().toString().trim();
        String barCode = barCodeEditText.getText().toString().trim();
        if (orderListView.getVisibility() == View.VISIBLE) {
            fillScan();
        } else {
            if (!TextUtils.isEmpty(storeId) || !TextUtils.isEmpty(containerId) || !TextUtils.isEmpty(barCode)) {
                DialogTools.showTwoButtonDialog(that, "退出将清除已经输入的内容,确定离开吗", "取消", "确定", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, true);
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            submit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (orderList != null && orderList.size() > position) {
            String orderId = orderList.get(position);
            String storeId = storeIdEditText.getText().toString().trim();
            String containerId = containerIdEditText.getText().toString().trim();
            String barCode = barCodeEditText.getText().toString().trim();
            new RequestGetOrderInfoTask(that, storeId, containerId, orderId, barCode).start();
        }
    }

    private class RequestGetOrderInfoTask extends HttpAsyncTask<StoreReceiptInfo> {

        private String storeId;

        private String containerId;

        private String orderOtherId;

        private String barCode;

        public RequestGetOrderInfoTask(Context context, String storeId, String containerId, String orderOtherId, String barCode) {
            super(context, true, true, true);
            this.storeId = storeId;
            this.containerId = containerId;
            this.barCode = barCode;
            this.orderOtherId = orderOtherId;
        }

        @Override
        public DataHull<StoreReceiptInfo> doInBackground() {
            return StoreInfoProvider.request(context, uId, uToken, storeId, containerId, orderOtherId, barCode);
        }

        @Override
        public void onPostExecute(StoreReceiptInfo result) {
            StoreInfoActivity.launch(StoreOpenActivity.this, uId, uToken, storeId, containerId, orderOtherId, barCode, result);
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
        }
    }

    private class RequestOrderListTask extends HttpAsyncTask<OrderList> {

        private String storeId;

        private String containerId;

        private String barCode;

        public RequestOrderListTask(Context context, String storeId, String containerId, String barCode) {
            super(context, true, true, true);
            this.storeId = storeId;
            this.containerId = containerId;
            this.barCode = barCode;
        }

        @Override
        public DataHull<OrderList> doInBackground() {
            return StoreOrderListProvider.request(context, uId, uToken, storeId, containerId, barCode);
        }

        @Override
        public void onPostExecute(OrderList result) {
            orderList = result;
            fillList();
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
        }

        @Override
        public void dataNull(String errMsg) {
            ToastTool.show(that, "没有可以收货的订单");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onComplete() {
        submitButton.setEnabled(true);
    }

    @Override
    public void onError(ContentEditText editText) {
        submitButton.setEnabled(false);
    }

    private class OrderListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (orderList == null) {
                return 0;
            }
            return orderList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHodler viewHodler;
            if (convertView == null) {
                View view = View.inflate(that, R.layout.storeorder_item, null);
                viewHodler = new ViewHodler();
                viewHodler.orderTextView = (TextView) view.findViewById(R.id.orderOtherId_TextView);

                view.setTag(viewHodler);
                convertView = view;
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            viewHodler.orderTextView.setText("订单：" + orderList.get(position));

            return convertView;
        }

        private class ViewHodler {

            private TextView orderTextView;

        }
    }
}
