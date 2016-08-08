package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.QcCreate;
import com.elianshang.code.reader.bean.QcList;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QualityControlActivity extends BaseActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, QualityControlActivity.class);
        activity.startActivity(intent);
    }

    private Toolbar mToolbar;

    private View createLayout;

    private ScanEditText createContainerIdEditText;

    private View detailLayout;

    private TextView detailProgressTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailQtyTextView;

    private ContentEditText detailInputQtyTextView;

    private Button detailSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private View waitLayout;

    private QcList qcList;

    private String containerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualitycontrol);

        findView();
        fillCreateData();
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
        createLayout = findViewById(R.id.create_Layout);
        createContainerIdEditText = (ScanEditText) createLayout.findViewById(R.id.containerId_EditText);

        detailLayout = findViewById(R.id.detail_Layout);
        detailProgressTextView = (TextView) detailLayout.findViewById(R.id.progress_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailQtyTextView = (TextView) detailLayout.findViewById(R.id.qty_TextView);
        detailInputQtyTextView = (ContentEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);

        waitLayout = findViewById(R.id.wait_Layout);

        detailSubmitButton.setOnClickListener(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fillCreateData() {
        createLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.GONE);

        scanEditTextTool = new ScanEditTextTool(this, createContainerIdEditText);
        scanEditTextTool.setComplete(this);

        createContainerIdEditText.requestFocus();
    }

    private void fillWaitData() {
        createLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.VISIBLE);
    }

    private void fillDetailData(QcList.Item item) {
        createLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
        waitLayout.setVisibility(View.GONE);

        detailItemNameTextView.setText(item.getItemName());
        detailPackNameTextView.setText(item.getPackName());
        detailQtyTextView.setText("" + item.getQty());
        detailPackNameTextView.setText(item.getPackName());
    }


    private void fillDetailDataNull() {
        createLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailItemNameTextView.setText("该商品不该存在在本托盘");
        detailPackNameTextView.setText("请盘点数量,并记录");
        detailQtyTextView.setText("如果是误扫,不要输入数量,或者输入0");
        detailPackNameTextView.setText("未知");
    }

    private void findItem(String barcode) {
        if (qcList != null && qcList.size() > 0) {
            for (QcList.Item item : qcList) {
                if (TextUtils.equals(barcode, item.getBarCode())) {
                    fillDetailData(item);
                    return;
                }
            }

            fillDetailDataNull();
        }
    }


    @Override
    public void onClick(View v) {
        if (detailSubmitButton == v) {
            new ConfirmAllTask(this, containerId, null).start();
        }
    }

    @Override
    public void onSetComplete() {
        String containerId = createContainerIdEditText.getText().toString();
        this.containerId = containerId;
        new QCCreateTaskTask(this, containerId).start();
    }

    @Override
    public void onInputError(int i) {

    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (createLayout.getVisibility() == View.VISIBLE) {
            if (scanEditTextTool != null) {
                scanEditTextTool.setScanText(s);
            }
        } else {
            findItem(s);
        }
    }

    private class ConfirmAllTask extends HttpAsyncTask<ResponseState> {

        private String containerId;

        private String qcList;

        public ConfirmAllTask(Context context, String containerId, String qcList) {
            super(context, true, true);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("code", "6919892880222");
                jsonObject.put("qty", "4");

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.containerId = containerId;
            this.qcList = jsonArray.toString();
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.qcConfirmAll(containerId, qcList);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {

        }
    }

    private class ScanContainerTask extends HttpAsyncTask<QcList> {

        private String containerId;

        public ScanContainerTask(Context context, String containerId) {
            super(context, true, true);

            this.containerId = containerId;
        }

        @Override
        public DataHull<QcList> doInBackground() {
            return HttpApi.qcScanContainer(containerId);
        }

        @Override
        public void onPostExecute(int updateId, QcList result) {
            qcList = result;
            fillWaitData();
        }
    }

    private class QCCreateTaskTask extends HttpAsyncTask<QcCreate> {

        private String containerId;

        public QCCreateTaskTask(Context context, String containerId) {
            super(context, true, true);

            this.containerId = containerId;
        }

        @Override
        public DataHull<QcCreate> doInBackground() {
            return HttpApi.qcCreateTask(containerId);
        }

        @Override
        public void onPostExecute(int updateId, QcCreate result) {
            new ScanContainerTask(context, containerId).start();
        }
    }
}