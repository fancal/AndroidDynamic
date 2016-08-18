package com.elianshang.wms.app.takestock.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.wms.app.takestock.R;
import com.elianshang.wms.app.takestock.bean.ResponseState;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.StockTakingDetailProvider;
import com.elianshang.wms.app.takestock.provider.StockTakingDoneProvider;
import com.elianshang.wms.app.takestock.provider.StockTakingProvider;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class TakeStockActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context, String uid) {
        StockTakingAssignTask task = new StockTakingAssignTask(context, uid);
        task.start();
    }

    /**
     * 任务列表,上一页传入
     */
    private TakeStockList takeStockList;

    /**
     * 当前执行的任务序号
     */
    private int progress = 0;

    /**
     * 任务进度TextView
     */
    private TextView progressTextView;

    /**
     * 任务布局 任务TextView
     */
    private TextView taskCodeTextView;

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
    private ScanEditText taskLocationIdEditText;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_takestock);

        readExtra();
        findView();
        fillNewTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void findView() {
        progressTextView = (TextView) findViewById(R.id.progress_TextView);
        taskCodeTextView = (TextView) findViewById(R.id.taskCode_TextView);

        taskLayout = findViewById(R.id.task_Layout);
        taskLocationCodeTextView = (TextView) taskLayout.findViewById(R.id.locationCode_TextView);
        taskLocationIdEditText = (ScanEditText) taskLayout.findViewById(R.id.locationId_EditText);

        detailLayout = findViewById(R.id.detail_Layout);
        detailLocationCodeTextView = (TextView) detailLayout.findViewById(R.id.locationCode_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailSystemQtyTextView = (TextView) detailLayout.findViewById(R.id.systemQty_TextView);
        detailAddButton = (Button) detailLayout.findViewById(R.id.add_Button);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);
        detailInputLayout = (LinearLayout) detailLayout.findViewById(R.id.input_Layout);

        detailAddButton.setOnClickListener(this);
        detailSubmitButton.setOnClickListener(this);
    }

    private void readExtra() {
        Intent intent = getIntent();
        takeStockList = (TakeStockList) intent.getSerializableExtra("list");
    }

    private void fillNewTask() {
        taskLocationIdEditText.setText("");
        detailInputLayout.removeAllViews();

        final TakeStockList.TakeStockTask task = takeStockList.get(progress);

        progressTextView.setText((progress + 1) + "/" + takeStockList.size());

        taskLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        taskCodeTextView.setText(task.getTaskId());
        taskLocationCodeTextView.setText(task.getLocationCode());

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        vhList.clear();
        scanEditTextTool = new ScanEditTextTool(that, taskLocationIdEditText);

        scanEditTextTool.setComplete(new ScanEditTextTool.OnStateChangeListener() {
            @Override
            public void onComplete() {
                String locationId = taskLocationIdEditText.getText().toString();
                if (TextUtils.equals(locationId, task.getLocationId())) {
                    new StockTakingGetTask(TakeStockActivity.this.that, task.getTaskId(), locationId).start();
                } else {
                    Toast.makeText(TakeStockActivity.this.that, "错误的库位,请扫描正确库位", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ContentEditText editText) {

            }
        });
    }

    private void fillTaskDetail(TakeStockDetail takeStockDetail) {
        taskLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        taskCodeTextView.setText(takeStockDetail.getTaskId());
        detailLocationCodeTextView.setText(takeStockDetail.getLocationId());
        detailItemNameTextView.setText(takeStockDetail.getItemName());
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
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (detailAddButton == v) {
            addItemView();
        } else if (detailSubmitButton == v) {
            submit();
        }
    }

    private void submit() {
        boolean state = true;
        String taskId = taskCodeTextView.getText().toString();
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonarray = new JSONArray();

        try {
            jsonObject.put("taskId", taskId);

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
                DialogTools.showTwoButtonDialog(that, "请确定,库位没有商品", "取消", "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new StockTakingDoOneTask(TakeStockActivity.this.that, jsonObject.toString()).start();
                    }
                }, false);
            } else {
                DialogTools.showTwoButtonDialog(this.that, "信息不完全的的数据,提交时将会被忽略", "取消", "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new StockTakingDoOneTask(TakeStockActivity.this.that, jsonObject.toString()).start();
                    }
                }, false);
            }
        }
    }

    private class ViewHolder {
        ScanEditText nameEditText;
        QtyEditText qtyEditText;
    }

    /**
     * 领取盘点任务
     */
    private static class StockTakingAssignTask extends HttpAsyncTask<TakeStockList> {

        private String uid;

        public StockTakingAssignTask(Context context, String uid) {
            super(context, true, true);
            this.uid = uid;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return StockTakingProvider.request(uid);
        }

        @Override
        public void onPostExecute(int updateId, TakeStockList result) {
            Intent intent = new Intent(context, TakeStockActivity.class);
            intent.putExtra("list", result);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }

        @Override
        public void netNull() {
            super.netNull();
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    /**
     * 盘点任务详情
     */
    private class StockTakingGetTask extends HttpAsyncTask<TakeStockDetail> {

        private String taskId;

        private String locationId;

        public StockTakingGetTask(Context context, String taskId, String locationId) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationId = locationId;
        }

        @Override
        public DataHull<TakeStockDetail> doInBackground() {
            return StockTakingDetailProvider.request(taskId, locationId);
        }

        @Override
        public void onPostExecute(int updateId, TakeStockDetail result) {
            fillTaskDetail(result);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
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
            return StockTakingDoneProvider.request(resultList);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            progress++;

            if (progress == takeStockList.size()) {
                finish();
            } else {
                fillNewTask();
            }
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }
}