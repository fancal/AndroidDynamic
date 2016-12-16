package com.elianshang.wms.app.takestock.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.takestock.R;
import com.elianshang.wms.app.takestock.bean.ResponseState;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.DoOneProvider;
import com.elianshang.wms.app.takestock.provider.ViewProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class TakeStockActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnStateChangeListener, AdapterView.OnItemClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, TakeStockList list, TakeStockDetail detail) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TakeStockActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (list != null) {
            intent.putExtra("list", list);
        }
        if (detail != null) {
            intent.putExtra("detail", detail);
        }
        activity.startPluginActivityForResult(intent, 1);
    }

    private String uId;

    private String uToken;

    /**
     * 任务列表,上一页传入
     */
    private TakeStockList takeStockList;

    private TakeStockList.TakeStockTask curTask;

    /**
     * 当前执行的任务序号
     */
    private int progress = 0;

    /**
     * 1  流式   2  列表式
     */
    private int workMode = 1;

    private Toolbar mToolbar;

    private TextView menuView;

    /**
     * 任务进度TextView
     */
    private TextView progressTextView;
    private View progressLayout;

    /**
     * 任务布局 任务TextView
     */
    private TextView taskCodeTextView;
    private View taskCodeLayout;

    private ListView locationCodeListView;

    /**
     * 任务布局
     */
    private View taskLayout;

    /**
     * 任务布局 库位TextView
     */
    private TextView taskLocationCodeTextView;

    /**
     * 任务布局 库位扫描输入框
     */
    private ScanEditText taskLocationCodeEditText;

    /**
     * 详情布局
     */
    private View detailLayout;

    /**
     * 详情布局 库位TextView
     */
    private TextView detailLocationCodeTextView;

    /**
     * 详情布局 名称TextView
     */
    private TextView detailItemNameTextView;

    private TextView detailBarcodeTextView;

    private TextView detailSkuCodeTextView;

    /**
     * 详情布局 规格TextView
     */
    private TextView detailPackNameTextView;

    /**
     * 详情布局 数量TextView
     */
    private TextView detailSystemQtyTextView;

    /**
     * 详情布局 添加按钮
     */
    private Button detailAddButton;

    /**
     * 详情布局 提交按钮
     */
    private Button detailSubmitButton;

    /**
     * 详情布局 输入布局
     */
    private LinearLayout detailInputLayout;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    /**
     * 动态布局记录列表
     */
    private ArrayList<ViewHolder> vhList = new ArrayList();

    private String serialNumber;

    private LocationCodeListAdapter adapter;

    private boolean isItemClick = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.takestock_activity_main);

        if (readExtra()) {
            findView();
            fillInit();
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void findView() {
        progressLayout = findViewById(R.id.progress_Layout);
        taskCodeLayout = findViewById(R.id.taskCode_Layout);
        progressTextView = (TextView) findViewById(R.id.progress_TextView);
        taskCodeTextView = (TextView) findViewById(R.id.taskCode_TextView);

        locationCodeListView = (ListView) findViewById(R.id.locationList_ListView);
        taskLayout = findViewById(R.id.task_Layout);
        taskLocationCodeTextView = (TextView) taskLayout.findViewById(R.id.locationCode_TextView);
        taskLocationCodeEditText = (ScanEditText) taskLayout.findViewById(R.id.locationCode_EditText);
        taskLocationCodeEditText.setCode(true);
        detailLayout = findViewById(R.id.detail_Layout);
        detailLocationCodeTextView = (TextView) detailLayout.findViewById(R.id.locationCode_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailBarcodeTextView = (TextView) detailLayout.findViewById(R.id.barcode_TextView);
        detailSkuCodeTextView = (TextView) detailLayout.findViewById(R.id.skuCode_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailSystemQtyTextView = (TextView) detailLayout.findViewById(R.id.systemQty_TextView);
        detailAddButton = (Button) detailLayout.findViewById(R.id.add_Button);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);
        detailInputLayout = (LinearLayout) detailLayout.findViewById(R.id.input_Layout);

        detailAddButton.setOnClickListener(this);
        detailSubmitButton.setOnClickListener(this);
        locationCodeListView.setOnItemClickListener(this);

        detailAddButton.setVisibility(View.GONE);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        menuView = (TextView) findViewById(R.id.menu_item);
        menuView.setOnClickListener(this);
        menuView.setVisibility(View.GONE);
    }

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillInit() {
        Intent intent = getIntent();
        takeStockList = (TakeStockList) intent.getSerializableExtra("list");
        if (takeStockList == null) {
            TakeStockDetail detail = (TakeStockDetail) intent.getSerializableExtra("detail");
            fillTaskDetail(detail);
        } else {
            for (int i = 0; i < takeStockList.size(); i++) {
                TakeStockList.TakeStockTask task = takeStockList.get(i);
                if ("3".equals(task.getStatus())) {
                    progress++;
                }
            }

            fillNewTask();
        }
    }

    private void fillListView() {
        if (workMode == 2 && takeStockList != null) {
            locationCodeListView.setVisibility(View.VISIBLE);
            taskLayout.setVisibility(View.GONE);
            detailLayout.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            taskCodeLayout.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new LocationCodeListAdapter();
                locationCodeListView.setAdapter(adapter);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void fillOneTask() {
        taskLayout.setVisibility(View.VISIBLE);
        locationCodeListView.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);

        if (takeStockList == null) {
            progressLayout.setVisibility(View.GONE);
        } else {
            progressLayout.setVisibility(View.VISIBLE);
            progressTextView.setText(String.valueOf(progress));
        }

        if (TextUtils.isEmpty(curTask.getTaskId())) {
            taskCodeLayout.setVisibility(View.GONE);
        } else {
            taskCodeLayout.setVisibility(View.VISIBLE);
            taskCodeTextView.setText(curTask.getTaskId());
        }
        taskLocationCodeTextView.setText(curTask.getLocationCode());

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        taskLocationCodeEditText.setText("");
        scanEditTextTool = new ScanEditTextTool(that, taskLocationCodeEditText);
        scanEditTextTool.setComplete(this);

    }

    private void fillNewTask() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        if (takeStockList != null) {
            menuView.setVisibility(View.VISIBLE);

            if (workMode == 1) {
                menuView.setText("列表");
            } else if (workMode == 2) {
                menuView.setText("流式");
            }
        }

        if (workMode == 2) {
            fillListView();
        } else if (workMode == 1) {
            for (int i = 0; i < takeStockList.size(); i++) {
                TakeStockList.TakeStockTask task = takeStockList.get(i);
                if (!"3".equals(task.getStatus())) {
                    curTask = task;
                    fillOneTask();
                    return;
                }
            }

            that.setResult(RESULT_OK);
            finish();
            ToastTool.show(that, "盘点完成");
        }
    }

    private void fillTaskDetail(TakeStockDetail takeStockDetail) {
        taskLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
        locationCodeListView.setVisibility(View.GONE);
        menuView.setVisibility(View.GONE);
        detailInputLayout.removeAllViews();

        if (takeStockList == null) {
            progressLayout.setVisibility(View.GONE);
        } else {
            progressLayout.setVisibility(View.VISIBLE);
            progressTextView.setText(String.valueOf(progress));
        }

        if (TextUtils.isEmpty(takeStockDetail.getTaskId())) {
            taskCodeLayout.setVisibility(View.GONE);
        } else {
            taskCodeLayout.setVisibility(View.VISIBLE);
            taskCodeTextView.setText(takeStockDetail.getTaskId());
        }

        taskCodeTextView.setText(takeStockDetail.getTaskId());
        detailLocationCodeTextView.setText(takeStockDetail.getLocationCode());
        detailItemNameTextView.setText(takeStockDetail.getItemName());
        detailBarcodeTextView.setText(takeStockDetail.getBarcode());
        detailSkuCodeTextView.setText(takeStockDetail.getSkuCode());
        detailPackNameTextView.setText(takeStockDetail.getPackName());

        if ("1".equals(takeStockDetail.getViewType())) {
            detailSystemQtyTextView.setText(takeStockDetail.getQty());
        } else {
            detailSystemQtyTextView.setText("***");
        }

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        vhList.clear();
        scanEditTextTool = new ScanEditTextTool(that);

        addItemView();
    }

    private void addItemView() {
        View view = View.inflate(that, R.layout.takestock_input_item, null);

        ScanEditText nameEditText = (ScanEditText) view.findViewById(R.id.barcode_edittext);
        QtyEditText qtyEditText = (QtyEditText) view.findViewById(R.id.realityqty_edittext);

        scanEditTextTool.addEditText(nameEditText, qtyEditText);

        detailInputLayout.addView(view);
        nameEditText.requestFocus();

        ViewHolder vh = new ViewHolder();
        vh.nameEditText = nameEditText;
        vh.qtyEditText = qtyEditText;
        vhList.add(vh);
    }


    @Override
    public void onBackPressed() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            if (takeStockList != null) {
                fillNewTask();
            } else {
                finish();
            }
            return;
        }

        DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onClick(View v) {
        if (detailAddButton == v) {
            addItemView();
        } else if (detailSubmitButton == v) {
            submit();
        } else if (menuView == v) {
            if (workMode == 1) {
                if (takeStockList != null) {
                    workMode = 2;
                    menuView.setText("流式");
                }
                fillNewTask();
            } else if (workMode == 2) {
                workMode = 1;
                menuView.setText("列表");
                fillNewTask();
            }
        }
    }

    private void submit() {
        boolean state = true;
        String taskId = taskCodeTextView.getText().toString();
        String locationCode = detailLocationCodeTextView.getText().toString();
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonarray = new JSONArray();

        try {
            jsonObject.put("taskId", taskId);
            jsonObject.put("locationCode", locationCode);

            for (ViewHolder vh : vhList) {
                String barCode = vh.nameEditText.getText().toString();
                String qty = vh.qtyEditText.getValue();

                if (TextUtils.isEmpty(barCode) || TextUtils.isEmpty(qty)) {
                    state = false;
                } else {
                    JSONObject jso = new JSONObject();
                    jso.put("barcode", barCode);
                    jso.put("qty", qty);
                    jsonarray.put(jso);
                }
            }

            jsonObject.put("list", jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (state) {
            new StockTakingDoOneTask(that, jsonObject.toString()).start();
        } else {
            if (jsonarray.length() == 0) {
                DialogTools.showTwoButtonDialog(that, "请确定,库位没有商品", "取消", "确认", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new StockTakingDoOneTask(TakeStockActivity.this.that, jsonObject.toString()).start();
                    }
                }, false);
            } else {
                DialogTools.showTwoButtonDialog(this.that, "信息不完全的的数据,提交时将会被忽略", "取消", "确认", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new StockTakingDoOneTask(TakeStockActivity.this.that, jsonObject.toString()).start();
                    }
                }, false);
            }
        }
    }

    @Override
    public void onComplete() {
        String taskId = taskCodeTextView.getText().toString();
        String locationCode = taskLocationCodeEditText.getText().toString();
        if (TextUtils.equals(locationCode, curTask.getLocationCode())) {
            new StockTakingGetTask(TakeStockActivity.this.that, taskId, locationCode).start();
        } else {
            Toast.makeText(TakeStockActivity.this.that, "错误的库位,请扫描正确库位", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isItemClick) {
            TakeStockList.TakeStockTask task = takeStockList.get(position);
            curTask = task;

            new StockTakingGetTask(TakeStockActivity.this.that, task.getTaskId(), task.getLocationCode()).start();
        }
    }

    private class ViewHolder {
        ScanEditText nameEditText;
        QtyEditText qtyEditText;
    }

    /**
     * 盘点任务详情
     */
    private class StockTakingGetTask extends HttpAsyncTask<TakeStockDetail> {

        private String taskId;
        private String locationCode;

        public StockTakingGetTask(Context context, String taskId, String locationCode) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationCode = locationCode;
            isItemClick = true;
        }

        @Override
        public DataHull<TakeStockDetail> doInBackground() {
            return ViewProvider.request(context, uId, uToken, taskId, locationCode);
        }

        @Override
        public void onPostExecute(TakeStockDetail result) {
            fillTaskDetail(result);
            isItemClick = false;
        }

        @Override
        public void netNull() {
            super.netNull();
            isItemClick = false;
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            isItemClick = false;
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            isItemClick = false;
        }
    }

    /**
     * 提交盘点任务一条
     */
    private class StockTakingDoOneTask extends HttpAsyncTask<ResponseState> {

        private String resultList;

        public StockTakingDoOneTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return DoOneProvider.request(context, uId, uToken, resultList, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            progress++;
            if (curTask != null) {
                curTask.setStatus("3");
            }

            if (takeStockList == null || progress == takeStockList.size()) {
                that.setResult(RESULT_OK);
                finish();
                ToastTool.show(context, "盘点完成");
            } else {
                fillNewTask();
            }
        }
    }

    private class LocationCodeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (takeStockList == null) {

            }
            return takeStockList.size();
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
            TakeStockList.TakeStockTask task = takeStockList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(that, R.layout.takestock_location_item, null);

                viewHolder = new ViewHolder();

                viewHolder.locationCodeTextView = (TextView) convertView.findViewById(R.id.locationCode_TextView);
                viewHolder.statusTextView = (TextView) convertView.findViewById(R.id.status_TextView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.locationCodeTextView.setText(task.getLocationCode());
            viewHolder.statusTextView.setText(TextUtils.equals("3", task.getStatus()) ? "已盘点" : "");

            return convertView;
        }

        class ViewHolder {

            TextView locationCodeTextView;

            TextView statusTextView;
        }
    }
}