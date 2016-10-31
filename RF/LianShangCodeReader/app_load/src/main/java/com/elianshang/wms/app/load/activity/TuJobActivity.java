package com.elianshang.wms.app.load.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.adapter.TuJobListAdapter;
import com.elianshang.wms.app.load.bean.ContainerInfo;
import com.elianshang.wms.app.load.bean.ResponseState;
import com.elianshang.wms.app.load.bean.TuJobList;
import com.elianshang.wms.app.load.provider.ConfirmProvier;
import com.elianshang.wms.app.load.provider.ContainerInfoProvier;
import com.elianshang.wms.app.load.provider.ContainerSubmitProvier;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class TuJobActivity extends DLBasePluginActivity implements View.OnClickListener, ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, AdapterView.OnItemClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, String tu, TuJobList tuJobList) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TuJobActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("tu", tu);
        intent.putExtra("tuJobList", tuJobList);
        activity.startPluginActivityForResult(intent, 1);
    }

    private Toolbar toolbar;

    private View listLayout;

    private ListView listView;

    private Button listFinishButton;

    private Button listSplitButton;

    private View tuJobLayout;

    private Button tuJobNextButton;

    private Button tuJobFinishButton;

    private View scanLayout;

    private ScanEditText scanContainerCodeEditText;

    private View nextLayout;

    private TextView nextContainerIdTextView;

    private TextView nexBoxNumTextView;

    private TextView nexTurnOverBoxNumTextView;

    private String uId;

    private String uToken;

    private String tu;

    private TuJobList tuJobList;

    private TuJobListAdapter adapter;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;

    private ContainerInfo containerInfo;

    private ContainerInfoTask containerInfoTask;

    private ContainerSubmitTask containerSubmitTask;

    private ConfirmTask confirmTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tujob);
        if (readExtras()) {
            findView();
            if (tuJobList != null && tuJobList.size() > 0) {
                fillListLayout();
            } else {
                fillScanLayout();
            }
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        tu = getIntent().getStringExtra("tu");
        tuJobList = (TuJobList) getIntent().getSerializableExtra("tuJobList");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findView() {
        initToolbar();

        listLayout = findViewById(R.id.list_layout);
        listView = (ListView) listLayout.findViewById(R.id.listview);
        listFinishButton = (Button) listLayout.findViewById(R.id.finish_Button);
        listSplitButton = (Button) listLayout.findViewById(R.id.split_Button);

        tuJobLayout = findViewById(R.id.tuJob_layout);
        tuJobNextButton = (Button) tuJobLayout.findViewById(R.id.next_Button);
        tuJobFinishButton = (Button) tuJobLayout.findViewById(R.id.finish_Button);

        scanLayout = findViewById(R.id.scan_layout);
        scanContainerCodeEditText = (ScanEditText) scanLayout.findViewById(R.id.containerId_EditText);

        nextLayout = findViewById(R.id.next_layout);
        nextContainerIdTextView = (TextView) nextLayout.findViewById(R.id.containerId_TextView);
        nexBoxNumTextView = (TextView) nextLayout.findViewById(R.id.boxNum_TextView);
        nexTurnOverBoxNumTextView = (TextView) nextLayout.findViewById(R.id.turnOverBoxNum_TextView);

        listFinishButton.setOnClickListener(this);
        listSplitButton.setOnClickListener(this);
        tuJobFinishButton.setOnClickListener(this);
        tuJobNextButton.setOnClickListener(this);
        tuJobNextButton.setEnabled(false);
    }

    private void fillListLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        listLayout.setVisibility(View.VISIBLE);
        tuJobLayout.setVisibility(View.GONE);


        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        if (adapter == null) {
            adapter = new TuJobListAdapter(that);
            listView.setAdapter(adapter);
        }
        adapter.setTuJobList(tuJobList);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);

        listSplitButton.setVisibility(tuJobList.isOpenSwitch() ? View.VISIBLE : View.GONE);
    }

    private void fillScanLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        listLayout.setVisibility(View.GONE);
        tuJobLayout.setVisibility(View.VISIBLE);
        scanLayout.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.GONE);
        tuJobNextButton.setVisibility(View.GONE);
        tuJobFinishButton.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        scanContainerCodeEditText.setText(null);
        scanContainerCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, scanContainerCodeEditText);
        scanEditTextTool.setComplete(this);

    }

    private void fillNextLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        listLayout.setVisibility(View.GONE);
        tuJobLayout.setVisibility(View.VISIBLE);
        scanLayout.setVisibility(View.GONE);
        nextLayout.setVisibility(View.VISIBLE);
        tuJobNextButton.setVisibility(View.VISIBLE);
        tuJobFinishButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        nextContainerIdTextView.setText("托盘码:" + containerInfo.getContainerId());
        nexBoxNumTextView.setText("总箱数" + containerInfo.getBoxNum());
        nexTurnOverBoxNumTextView.setText("总周转箱数:" + containerInfo.getTurnoverBoxNum());

        tuJobNextButton.setText(containerInfo.isLoaded() ? "知道了" : "下一步");
        tuJobNextButton.setEnabled(true);
    }

    private void fillTuJobLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        listLayout.setVisibility(View.GONE);
        tuJobLayout.setVisibility(View.VISIBLE);
        scanLayout.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.VISIBLE);
        tuJobNextButton.setVisibility(View.VISIBLE);
        tuJobFinishButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        scanContainerCodeEditText.setText(null);
        scanContainerCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, scanContainerCodeEditText);
        scanEditTextTool.setComplete(this);

        nextContainerIdTextView.setText("托盘码:" + containerInfo.getContainerId());
        nexBoxNumTextView.setText("总箱数" + containerInfo.getBoxNum());
        nexTurnOverBoxNumTextView.setText("总周转箱数:" + containerInfo.getTurnoverBoxNum());

        tuJobNextButton.setText(containerInfo.isLoaded() ? "知道了" : "下一步");
        tuJobNextButton.setEnabled(false);
    }

    private void popNext() {
        checkContainerInfo();
        if (tuJobList != null && tuJobList.size() > 0) {
            fillListLayout();
        } else {
            fillScanLayout();
        }
    }

    private void checkContainerInfo() {
        if (containerInfo == null || !containerInfo.isLoaded()) {
            return;
        }
        if (tuJobList != null && tuJobList.size() > 0) {
            for (TuJobList.Item item : tuJobList.getTuJobList()) {
                if (TextUtils.equals(item.getMarkContainerId(), containerInfo.getContainerId())) {
                    tuJobList.remove(item);
                    break;
                }
            }
        }
    }

    private void requestContainerInfo(String containerId) {
        if (containerInfoTask != null) {
            containerInfoTask.cancel();
            containerInfoTask = null;
        }
        containerInfoTask = new ContainerInfoTask(that, containerId);
        containerInfoTask.start();
    }

    private void requestContainerSubmit(String containerId) {
        if (containerSubmitTask != null) {
            containerSubmitTask.cancel();
            containerSubmitTask = null;
        }
        containerSubmitTask = new ContainerSubmitTask(that, containerId);
        containerSubmitTask.start();
    }

    private void requestConfirm() {

        if (confirmTask != null) {
            confirmTask.cancel();
            confirmTask = null;
        }
        confirmTask = new ConfirmTask(that);
        confirmTask.start();
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    public void onBackPressed() {
        if (nextLayout.getVisibility() == View.VISIBLE && tuJobList != null && tuJobList.size() > 0) {
            fillListLayout();
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == listFinishButton) {
            requestConfirm();
        } else if (v == tuJobFinishButton) {
            requestConfirm();
        } else if (v == tuJobNextButton) {
            if (containerInfo != null) {
                if (!containerInfo.isLoaded()) {
                    requestContainerSubmit(containerInfo.getContainerId());
                } else {
                    popNext();
                }
            }
        } else if (v == listSplitButton) {
            if (tuJobList != null) {
                tuJobList.clear();
            }
            fillScanLayout();
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
        if (scanLayout.getVisibility() == View.VISIBLE) {
            Editable editable = scanContainerCodeEditText.getText();
            if (editable != null) {
                requestContainerInfo(editable.toString());
            }

        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        requestContainerInfo(tuJobList.get(position).getMarkContainerId());
        TuJobList.Item item = tuJobList.get(position);
        containerInfo = new ContainerInfo();
        containerInfo.setContainerId(item.getMarkContainerId());
        containerInfo.setLoaded(item.isLoaded());
        containerInfo.setBoxNum(item.getBoxNum());
        containerInfo.setTurnoverBoxNum(item.getTurnoverBoxNum());

        fillTuJobLayout();
    }

    /**
     * 托盘码详情
     */
    private class ContainerInfoTask extends HttpAsyncTask<ContainerInfo> {

        private String containerId;

        public ContainerInfoTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<ContainerInfo> doInBackground() {
            return ContainerInfoProvier.request(context, uId, uToken, tu, containerId);
        }

        @Override
        public void onPostExecute(ContainerInfo result) {
            if (nextLayout.getVisibility() == View.VISIBLE && containerInfo != null) {
                if (!TextUtils.equals(containerInfo.getContainerId(), result.getContainerId())) {
                    ToastTool.show(that, "托盘码不一致");
                    tuJobNextButton.setEnabled(false);
                } else {
                    tuJobNextButton.setEnabled(true);
                }
                return;
            }
            containerInfo = result;
            checkContainerInfo();
            fillNextLayout();
        }
    }

    /**
     * 托盘码信息提交
     */
    private class ContainerSubmitTask extends HttpAsyncTask<ResponseState> {

        private String containerId;

        public ContainerSubmitTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ContainerSubmitProvier.request(context, uId, uToken, tu, containerId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            containerInfo.setLoaded(true);
            popNext();
        }
    }

    /**
     * 装车完毕
     */
    private class ConfirmTask extends HttpAsyncTask<ResponseState> {

        public ConfirmTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ConfirmProvier.request(context, uId, uToken, tu, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
