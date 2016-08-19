package com.elianshang.wms.app.qc.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.http.HttpApi;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.wms.app.qc.R;
import com.elianshang.wms.app.qc.bean.QcCreate;
import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.bean.ResponseState;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhanzhi on 16/8/10.
 */
public abstract class BaseQcController implements View.OnClickListener, ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    protected Activity activity;

    protected QcList qcList;

    protected String containerId;

    protected HashMap<String, CacheQty> submitMap = new HashMap();

    private Toolbar mToolbar;

    private View createLayout;

    private ScanEditText createContainerIdEditText;

    private ScanEditTextTool scanEditTextTool;

    protected LinearLayout mainView;

    protected Button submitButton;

    protected abstract void fillQcListData();

    protected abstract void onSubmitButtonClick();

    protected abstract void onScan(String s);

    public BaseQcController(Activity activity) {
        this.activity = activity;
        init();
    }

    protected void init() {
        initToolbar();
        initCreateLayout();
        initSubmitButton();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    private void initCreateLayout() {
        createLayout = activity.findViewById(R.id.create_Layout);
        createContainerIdEditText = (ScanEditText) createLayout.findViewById(R.id.containerId_EditText);
        mainView = (LinearLayout) activity.findViewById(R.id.qc_main);

        scanEditTextTool = new ScanEditTextTool(activity, createContainerIdEditText);
        scanEditTextTool.setComplete(this);

        createContainerIdEditText.requestFocus();
        createLayout.setVisibility(View.VISIBLE);
    }

    private void initSubmitButton() {
        submitButton = (Button) activity.findViewById(R.id.submit_Button);
        submitButton.setOnClickListener(this);
    }

    private void fillQcList() {
        createLayout.setVisibility(View.GONE);
        fillQcListData();
    }

    protected void releaseCreateLayout() {
        scanEditTextTool.release();
        scanEditTextTool = null;
    }

    @Override
    public final void onClick(View v) {
        if (submitButton == v) {
            onSubmitButtonClick();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void onComplete() {
        String containerId = createContainerIdEditText.getText().toString();
        this.containerId = containerId;

        //FIXME test
        new ScanContainerTask(activity, containerId).start();
//        new QCCreateTaskTask(activity, containerId).start();
    }

    @Override
    public final void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        } else {
            onScan(s);
        }
    }

    protected void checkSubmit() {
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

            DialogTools.showTwoButtonDialog(activity, msg, "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    submit();
                }
            }, true);
            return;
        }

        submit();
    }

    protected void submit() {
        try {
            HashMap<String, CacheQty> allMap = new HashMap();
            for (QcList.Item item : qcList) {
                if (!submitMap.containsKey(item.getBarCode())) {
                    CacheQty cacheQty = new CacheQty();
                    cacheQty.qty = "0";

                    allMap.put(item.getBarCode(), cacheQty);
                }
            }

            allMap.putAll(submitMap);

            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, CacheQty> entry : allMap.entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", entry.getKey());
                jsonObject.put("qty", entry.getValue().qty);
                if (!"0".equals(entry.getValue().exceptionQty) && !TextUtils.isEmpty(entry.getValue().exceptionQty) && !TextUtils.isEmpty(entry.getValue().exceptionType)) {
                    jsonObject.put("exceptionType", entry.getValue().exceptionType);
                    jsonObject.put("exceptionQty", entry.getValue().exceptionQty);
                }

                jsonArray.put(jsonObject);
            }

            new ConfirmAllTask(activity, containerId, jsonArray.toString()).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存数量记录
     */
    public static class CacheQty {

        public String qty = "0";

        public String exceptionType;

        public String exceptionQty = "0";
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
            releaseCreateLayout();
            fillQcList();
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
