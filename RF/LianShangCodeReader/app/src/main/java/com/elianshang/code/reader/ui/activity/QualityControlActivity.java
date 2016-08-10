package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.HashMap;
import java.util.Map;


public class QualityControlActivity extends BaseActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnStateChangeListener {

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

    private ContentEditText detailInputQtyEditText;

    private ContentEditText detailShoddynQtyEditText;

    private Button detailSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    private View waitLayout;

    private QcList qcList;

    /**
     * 记录本地QC操作列表
     */
    private HashMap<String, CacheQty> submitMap = new HashMap();

    /**
     * 当前商品条码
     */
    private String curBarCode;

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
        detailInputQtyEditText = (ContentEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailShoddynQtyEditText = (ContentEditText) detailLayout.findViewById(R.id.shoddyQty_EditView);
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
        detailProgressTextView.setText(submitMap.size() + "/" + qcList.size());

        createLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
        waitLayout.setVisibility(View.GONE);

        detailItemNameTextView.setText(item.getItemName());
        detailPackNameTextView.setText(item.getPackName());
        detailQtyTextView.setText("" + item.getQty());

        if (submitMap.containsKey(item.getBarCode())) {
            CacheQty cacheQty = submitMap.get(item.getBarCode());
            detailInputQtyEditText.setHint(null);
            detailInputQtyEditText.setText(String.valueOf(cacheQty.qty));
            detailShoddynQtyEditText.setHint(null);
            detailShoddynQtyEditText.setText(String.valueOf(cacheQty.exceptionQty));
        } else {
            detailInputQtyEditText.setText(null);
            detailInputQtyEditText.setHint(String.valueOf(item.getQty()));
            detailShoddynQtyEditText.setText(null);
            detailShoddynQtyEditText.setHint("0");
        }
    }

    /**
     * 填充QC列表不存在的商品
     */
    private void fillDetailDataNull(String barCode) {
        createLayout.setVisibility(View.GONE);
        waitLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailItemNameTextView.setText("该商品不该存在在本托盘(" + barCode + ")");
        detailPackNameTextView.setText("请按EA查点数量");
        detailQtyTextView.setText("如果是误扫,请输入0,或不要输入数量");
        detailInputQtyEditText.setText(null);
        detailInputQtyEditText.setHint(null);
    }

    private void findItem(String barcode) {
        noteItem();

        curBarCode = barcode;

        if (qcList != null && qcList.size() > 0) {
            for (QcList.Item item : qcList) {
                if (TextUtils.equals(barcode, item.getBarCode())) {
                    fillDetailData(item);
                    return;
                }
            }
            fillDetailDataNull(barcode);
        }
    }

    /**
     * 记录记录
     */
    private void noteItem() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            String inputQty = detailInputQtyEditText.getText().toString();
            String exceptionQty = detailShoddynQtyEditText.getText().toString();

            if (TextUtils.isEmpty(inputQty)) {
                inputQty = detailInputQtyEditText.getHint().toString();
            }

            if (TextUtils.isEmpty(exceptionQty)) {
                exceptionQty = detailShoddynQtyEditText.getHint().toString();
            }

            if (!TextUtils.isEmpty(inputQty) || !TextUtils.isEmpty(exceptionQty)) {
                float fiqty = Float.parseFloat(inputQty);
                float feqty = Float.parseFloat(exceptionQty);
                CacheQty cacheQty = new CacheQty();
                cacheQty.qty = fiqty;
                if (feqty != 0) {
                    cacheQty.exceptionQty = feqty;
                    cacheQty.exceptionType = 1;
                }

                submitMap.put(curBarCode, cacheQty);
            } else {
                submitMap.remove(curBarCode);
            }
        }
    }

    private void checkSubmit() {
        QcList missList = new QcList();
        for (QcList.Item item : qcList) {
            if (!submitMap.containsKey(item.getBarCode())) {
                missList.add(item);
            }
        }

        if (missList.size() > 0) {
            String msg = "未完成QC的商品列表:\n";

            for (QcList.Item item : missList) {
                msg += item.getItemName();
                msg += "  ";
                msg += item.getPackName();
                msg += " * ";
                msg += item.getQty();
                msg += "\n";
            }

            DialogTools.showTwoButtonDialog(this, msg, "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    submit();
                }
            }, true);
            return;
        }

        submit();
    }

    private void submit() {
        try {
            HashMap<String, CacheQty> allMap = new HashMap();
            for (QcList.Item item : qcList) {
                if (!submitMap.containsKey(item.getBarCode())) {
                    CacheQty cacheQty = new CacheQty();
                    cacheQty.qty = item.getQty();

                    allMap.put(item.getBarCode(), cacheQty);
                }
            }

            allMap.putAll(submitMap);

            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, CacheQty> entry : allMap.entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", entry.getKey());
                jsonObject.put("qty", entry.getValue().qty);
                if (entry.getValue().exceptionQty != 0 && entry.getValue().exceptionType != 0) {
                    jsonObject.put("exceptionType", entry.getValue().exceptionType);
                    jsonObject.put("exceptionQty", entry.getValue().exceptionQty);
                }

                jsonArray.put(jsonObject);
            }

            new ConfirmAllTask(this, containerId, jsonArray.toString()).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (detailSubmitButton == v) {
            noteItem();
            checkSubmit();
        }
    }

    @Override
    public void onComplete() {
        String containerId = createContainerIdEditText.getText().toString();
        this.containerId = containerId;
        new QCCreateTaskTask(this, containerId).start();
    }

    @Override
    public void onError(ContentEditText editText) {

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


    /**
     * 缓存数量记录
     */
    private class CacheQty {

        private float qty;

        private int exceptionType;

        private float exceptionQty;
    }

    /**
     * 提交QC数据
     */
    private class ConfirmAllTask extends HttpAsyncTask<ResponseState> {

        private String containerId;

        private String qcList;

        public ConfirmAllTask(Context context, String containerId, String qcList) {
            super(context, true, true);
            this.containerId = containerId;
            this.qcList = qcList;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.qcConfirmAll(containerId, qcList);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {

        }
    }

    /**
     * 获取QC列表
     */
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

    /**
     * 创建QC任务,测试接口
     */
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