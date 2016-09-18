package com.elianshang.wms.app.takestock.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.takestock.R;
import com.elianshang.wms.app.takestock.bean.ResponseState;
import com.elianshang.wms.app.takestock.bean.TakeStockDetail;
import com.elianshang.wms.app.takestock.bean.TakeStockList;
import com.elianshang.wms.app.takestock.provider.GetTaskProvider;
import com.elianshang.wms.app.takestock.provider.DoOneProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class TakeStockActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, TakeStockList list) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TakeStockActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("list", list);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    /**
     * 任务列表,上一页传入
     */
    private TakeStockList takeStockList;

    /**
     * 当前执行的任务序号
     */
    private int progress = 0;

    private Toolbar mToolbar;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_takestock);

        if (readExtra()) {
            findView();
            fillNewTask();
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
        progressTextView = (TextView) findViewById(R.id.progress_TextView);
        taskCodeTextView = (TextView) findViewById(R.id.taskCode_TextView);

        taskLayout = findViewById(R.id.task_Layout);
        taskLocationCodeTextView = (TextView) taskLayout.findViewById(R.id.locationCode_TextView);
        taskLocationCodeEditText = (ScanEditText) taskLayout.findViewById(R.id.locationCode_EditText);
        taskLocationCodeEditText.setCode(true);
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
    }

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        Intent intent = getIntent();
        takeStockList = (TakeStockList) intent.getSerializableExtra("list");

        return true;
    }

    private void fillNewTask() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        taskLocationCodeEditText.setText("");
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
        scanEditTextTool = new ScanEditTextTool(that, taskLocationCodeEditText);

        scanEditTextTool.setComplete(new ScanEditTextTool.OnStateChangeListener() {
            @Override
            public void onComplete() {
                String locationCode = taskLocationCodeEditText.getText().toString();
                if (TextUtils.equals(locationCode, task.getLocationCode())) {
                    new StockTakingGetTask(TakeStockActivity.this.that, task.getTaskId(), locationCode).start();
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
        detailLocationCodeTextView.setText(takeStockDetail.getLocationCode());
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
    public void onBackPressed() {
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
     * 盘点任务详情
     */
    private class StockTakingGetTask extends HttpAsyncTask<TakeStockDetail> {

        private String taskId;

        private String locationCode;

        public StockTakingGetTask(Context context, String taskId, String locationCode) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationCode = locationCode;
        }

        @Override
        public DataHull<TakeStockDetail> doInBackground() {
            return GetTaskProvider.request(context, uId, uToken, taskId, locationCode);
        }

        @Override
        public void onPostExecute(TakeStockDetail result) {
            fillTaskDetail(result);
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

            if (progress == takeStockList.size()) {
                finish();
                ToastTool.show(context, "盘点完成");
            } else {
                fillNewTask();
            }
        }
    }
}