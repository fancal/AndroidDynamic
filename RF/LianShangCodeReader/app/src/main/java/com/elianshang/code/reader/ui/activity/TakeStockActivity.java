package com.elianshang.code.reader.ui.activity;

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

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.TakeStockDetail;
import com.elianshang.code.reader.bean.TakeStockList;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TakeStockActivity extends BaseActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context, String uid) {
        TakeStockListTask task = new TakeStockListTask(context, uid);
        task.start();
    }

    private TakeStockList takeStockList;

    private int progress = 0;

    private TextView progressTextView;

    private View taskLayout;

    private TextView taskTaskIdTextView;

    private TextView taskLocationTextView;

    private ScanEditText taskLocationEditText;

    private View detailLayout;

    private TextView detailTaskIdTextView;

    private TextView detailLocationTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailSystemQtyTextView;

    private Button detailAddButton;

    private Button datailSubmitButton;

    private LinearLayout detailInputLayout;

    private ScanEditTextTool scanEditTextTool;

    private ArrayList<ViewHolder> vhList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_takestock);

        readExtra();
        findView();
        fillNewTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }

    private void findView() {
        progressTextView = (TextView) findViewById(R.id.progress_textview);

        taskLayout = findViewById(R.id.task_layout);
        taskTaskIdTextView = (TextView) taskLayout.findViewById(R.id.task_textview);
        taskLocationTextView = (TextView) taskLayout.findViewById(R.id.locationid_textview);
        taskLocationEditText = (ScanEditText) taskLayout.findViewById(R.id.locationid_edittext);

        detailLayout = findViewById(R.id.detail_layout);
        detailTaskIdTextView = (TextView) detailLayout.findViewById(R.id.task_textview);
        detailLocationTextView = (TextView) detailLayout.findViewById(R.id.locationid_textview);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemname_textview);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packname_textview);
        detailSystemQtyTextView = (TextView) detailLayout.findViewById(R.id.systemqty_textview);
        detailAddButton = (Button) detailLayout.findViewById(R.id.add_button);
        datailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_button);
        detailInputLayout = (LinearLayout) detailLayout.findViewById(R.id.input_layout);

        detailAddButton.setOnClickListener(this);
        datailSubmitButton.setOnClickListener(this);
    }

    private void readExtra() {
        Intent intent = getIntent();
        takeStockList = (TakeStockList) intent.getSerializableExtra("list");
    }

    private void fillNewTask() {
        final TakeStockList.TakeStockTask task = takeStockList.get(progress);

        progressTextView.setText((progress + 1) + "/" + takeStockList.size());

        taskLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        taskTaskIdTextView.setText(task.getTaskId());
        taskLocationTextView.setText(task.getLocationCode());

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        vhList.clear();
        scanEditTextTool = new ScanEditTextTool(this, taskLocationEditText);

        scanEditTextTool.setComplete(new ScanEditTextTool.OnSetComplete() {
            @Override
            public void onSetComplete() {
                String locationId = taskLocationEditText.getText().toString();
                if (TextUtils.equals(locationId, task.getLocationId())) {
                    new TakeStockDetailTask(TakeStockActivity.this, task.getTaskId(), locationId).start();
                } else {
                    Toast.makeText(TakeStockActivity.this, "错误的库位,请扫描正确库位", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onInputError(int i) {

            }
        });
    }

    private void fillTaskDetail(TakeStockDetail takeStockDetail) {
        taskLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailTaskIdTextView.setText(takeStockDetail.getTaskId());
        detailLocationTextView.setText(takeStockDetail.getLocationId());
        detailItemNameTextView.setText(takeStockDetail.getItemName());
        detailPackNameTextView.setText(takeStockDetail.getPackName());
        detailSystemQtyTextView.setText(takeStockDetail.getQty());

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        vhList.clear();
        scanEditTextTool = new ScanEditTextTool(this);

        addItemView();
    }

    private void addItemView() {
        View view = View.inflate(this, R.layout.takestock_input_item, null);

        ScanEditText nameEditText = (ScanEditText) view.findViewById(R.id.barcode_edittext);
        ContentEditText qtyEditText = (ContentEditText) view.findViewById(R.id.realityqty_edittext);

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
        } else if (datailSubmitButton == v) {
            submit();
        }
    }

    private void submit() {
        int state = 0;
        final JSONArray jsonarray = new JSONArray();
        String taskId = detailTaskIdTextView.getText().toString();
        for (ViewHolder vh : vhList) {
            String barCode = vh.nameEditText.getText().toString();
            String qty = vh.qtyEditText.getText().toString();

            if (TextUtils.isEmpty(barCode) || TextUtils.isEmpty(qty)) {
                state++;
            } else {
                try {
                    JSONObject jso = new JSONObject();
                    jso.put("taskId", taskId);
                    jso.put("barcode", barCode);
                    jso.put("qty", qty);

                    jsonarray.put(jso);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (state == 0) {
            new TakeStockSubmitTask(this, jsonarray.toString()).start();
        } else if (state == 1) {
            DialogTools.showTwoButtonDialog(this, "请确定,库位没有商品", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new TakeStockSubmitTask(TakeStockActivity.this, jsonarray.toString()).start();
                }
            }, false);
        } else {
            DialogTools.showTwoButtonDialog(this, "信息不完全的的数据,提交时将会被忽略", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new TakeStockSubmitTask(TakeStockActivity.this, jsonarray.toString()).start();
                }
            }, false);
        }
    }

    private class ViewHolder {
        ScanEditText nameEditText;
        ContentEditText qtyEditText;
    }

    private static class TakeStockListTask extends HttpAsyncTask<TakeStockList> {

        private String uid;

        public TakeStockListTask(Context context, String uid) {
            super(context, true, true);
            this.uid = uid;
        }

        @Override
        public DataHull<TakeStockList> doInBackground() {
            return HttpApi.inhouseStockTakingAssign(uid);
        }

        @Override
        public void onPostExecute(int updateId, TakeStockList result) {
            Intent intent = new Intent(context, TakeStockActivity.class);
            intent.putExtra("list", result);
            context.startActivity(intent);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }

    private class TakeStockDetailTask extends HttpAsyncTask<TakeStockDetail> {

        private String taskId;

        private String locationId;

        public TakeStockDetailTask(Context context, String taskId, String locationId) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationId = locationId;
        }

        @Override
        public DataHull<TakeStockDetail> doInBackground() {
            return HttpApi.inhouseStockTakingGetTask(taskId, locationId);
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

    private class TakeStockSubmitTask extends HttpAsyncTask<ResponseState> {

        private String resultList;

        public TakeStockSubmitTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.inhouseStockTakingDoOne(resultList);
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
