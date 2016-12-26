package com.elianshang.wms.app.load.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.adapter.ExpensiveListAdapter;
import com.elianshang.wms.app.load.adapter.TuJobListAdapter;
import com.elianshang.wms.app.load.bean.ContainerInfo;
import com.elianshang.wms.app.load.bean.ExpensiveList;
import com.elianshang.wms.app.load.bean.ResponseState;
import com.elianshang.wms.app.load.bean.TuJobList;
import com.elianshang.wms.app.load.provider.ConfirmProvier;
import com.elianshang.wms.app.load.provider.ContainerInfoProvier;
import com.elianshang.wms.app.load.provider.ContainerSubmitProvier;
import com.elianshang.wms.app.load.provider.ExpensiveListProvier;
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
        if (tuJobList != null) {
            intent.putExtra("tuJobList", tuJobList);
        }
        activity.startPluginActivityForResult(intent, 1);
    }

    private Toolbar toolbar;
    /**
     * 尾货
     */
    private View tujobListLayout;

    private ListView tujobListView;

    private Button tujobListFinishButton;

    private Button tujobListSplitButton;
    /**
     * 贵品
     */
    private View expensiveListLayout;

    private ListView expensiveListView;

    private Button expensiveListFinishButton;

    private TextView expensiveFinishText;

    /**
     * 开始装车
     */
    private View startLayout;

    private Button startNextButton;

    private Button startFinishButton;
    /**
     * 扫描
     */
    private View startScanLayout;

    private ScanEditText startScanContainerCodeEditText;
    /**
     * 详情
     */
    private View startDetailLayout;

    private TextView startDetailContainerIdTextView;

    private TextView startDetailBoxNumTextView;

    private TextView startDetailTurnOverBoxNumTextView;

    private String uId;

    private String uToken;

    private String tu;

    private TuJobList tuJobList;

    private TuJobListAdapter tuJobListAdapter;

    private ExpensiveListAdapter expensiveListAdapter;

    private ExpensiveList expensiveList;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;

    private ContainerInfo containerInfo;

    private TuJobList.Item tujobItem;

    private ExpensiveList.Item expensiveItem;

    private ContainerInfoTask containerInfoTask;

    private ContainerSubmitTask containerSubmitTask;

    private ConfirmTask confirmTask;

    private ExpensiveTask expensiveTask;

    private boolean isItemClick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity_tujob);
        if (readExtras()) {
            findView();
            if (tuJobList != null && tuJobList.size() > 0) {
                fillTujobListLayout();
            } else {
                fillStartScanLayout();
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

        tujobListLayout = findViewById(R.id.tujob_list_layout);
        tujobListView = (ListView) tujobListLayout.findViewById(R.id.listview);
        tujobListFinishButton = (Button) tujobListLayout.findViewById(R.id.finish_Button);
        tujobListSplitButton = (Button) tujobListLayout.findViewById(R.id.split_Button);

        expensiveListLayout = findViewById(R.id.expensive_list_layout);
        expensiveListView = (ListView) expensiveListLayout.findViewById(R.id.listview);
        expensiveListFinishButton = (Button) expensiveListLayout.findViewById(R.id.finish_Button);
        expensiveFinishText = (TextView) expensiveListLayout.findViewById(R.id.finish_text);

        startLayout = findViewById(R.id.start_layout);
        startNextButton = (Button) startLayout.findViewById(R.id.next_Button);
        startFinishButton = (Button) startLayout.findViewById(R.id.finish_Button);

        startScanLayout = findViewById(R.id.scan_layout);
        startScanContainerCodeEditText = (ScanEditText) startScanLayout.findViewById(R.id.containerId_EditText);

        startDetailLayout = findViewById(R.id.detail_layout);
        startDetailContainerIdTextView = (TextView) startDetailLayout.findViewById(R.id.containerId_TextView);
        startDetailBoxNumTextView = (TextView) startDetailLayout.findViewById(R.id.boxNum_TextView);
        startDetailTurnOverBoxNumTextView = (TextView) startDetailLayout.findViewById(R.id.turnOverBoxNum_TextView);

        tujobListFinishButton.setOnClickListener(this);
        tujobListSplitButton.setOnClickListener(this);
        expensiveListFinishButton.setOnClickListener(this);
        startFinishButton.setOnClickListener(this);
        startNextButton.setOnClickListener(this);
        startNextButton.setEnabled(false);
    }

    /**
     * 尾货列表
     */
    private void fillTujobListLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.VISIBLE);
        expensiveListLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);


        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        if (tuJobListAdapter == null) {
            tuJobListAdapter = new TuJobListAdapter(that);
            tujobListView.setAdapter(tuJobListAdapter);
        }
        tuJobListAdapter.setTuJobList(tuJobList);
        tuJobListAdapter.notifyDataSetChanged();
        tujobListView.setOnItemClickListener(this);

        tujobListSplitButton.setVisibility(tuJobList.isOpenSwitch() ? View.VISIBLE : View.GONE);
    }

    /**
     * 贵品列表
     */
    private void fillExpensiveListLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.GONE);
        expensiveListLayout.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.GONE);


        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        if (expensiveList != null && expensiveList.size() > 0) {
            if (expensiveListAdapter == null) {
                expensiveListAdapter = new ExpensiveListAdapter(that);
                expensiveListView.setAdapter(expensiveListAdapter);
            }
            expensiveListAdapter.setExpensiveList(expensiveList);
            expensiveListAdapter.notifyDataSetChanged();
            expensiveListView.setOnItemClickListener(this);

            expensiveListView.setVisibility(View.VISIBLE);
            expensiveFinishText.setVisibility(View.GONE);
        } else {
            expensiveListView.setVisibility(View.GONE);
            expensiveFinishText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开始装车-扫描
     */
    private void fillStartScanLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.GONE);
        expensiveListLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);

        startScanLayout.setVisibility(View.VISIBLE);
        startDetailLayout.setVisibility(View.GONE);
        startNextButton.setVisibility(View.GONE);
        startFinishButton.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        startScanContainerCodeEditText.setText(null);
        startScanContainerCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, startScanContainerCodeEditText);
        scanEditTextTool.setComplete(this);

    }

    /**
     * 开始装车-详情
     *
     * @param markContainerId
     */
    private void fillStartDetailLayout(String markContainerId) {
        if (containerInfo == null) {
            return;
        }
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.GONE);
        expensiveListLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);

        startScanLayout.setVisibility(View.GONE);
        startDetailLayout.setVisibility(View.VISIBLE);
        startNextButton.setVisibility(View.VISIBLE);
        startFinishButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        startDetailContainerIdTextView.setText("托盘码:" + markContainerId);
        startDetailBoxNumTextView.setText("总箱数" + containerInfo.getBoxNum());
        startDetailTurnOverBoxNumTextView.setText("总周转箱数:" + containerInfo.getTurnoverBoxNum());
        startNextButton.setText(containerInfo.isLoaded() ? "知道了" : "下一步");
        startNextButton.setEnabled(true);

    }

    /**
     * 开始装车-尾货详情
     */
    private void fillTuJobLayout() {
        if (tujobItem == null) {
            return;
        }
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.GONE);
        expensiveListLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);
        startScanLayout.setVisibility(View.VISIBLE);
        startDetailLayout.setVisibility(View.VISIBLE);
        startNextButton.setVisibility(View.VISIBLE);
        startFinishButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        startScanContainerCodeEditText.setText(null);
        startScanContainerCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, startScanContainerCodeEditText);
        scanEditTextTool.setComplete(this);

        startDetailContainerIdTextView.setText("托盘码:" + tujobItem.getMarkContainerId());
        startDetailBoxNumTextView.setText("总箱数" + tujobItem.getBoxNum());
        startDetailTurnOverBoxNumTextView.setText("总周转箱数:" + tujobItem.getTurnoverBoxNum());
        startNextButton.setText(tujobItem.isLoaded() ? "知道了" : "下一步");
        startNextButton.setEnabled(false);
    }

    /**
     * 开始装车-贵品详情
     */
    private void fillExpensiveLayout() {
        if (expensiveItem == null) {
            return;
        }
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        tujobListLayout.setVisibility(View.GONE);
        expensiveListLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);
        startScanLayout.setVisibility(View.VISIBLE);
        startDetailLayout.setVisibility(View.VISIBLE);
        startNextButton.setVisibility(View.VISIBLE);
        startFinishButton.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
        startScanContainerCodeEditText.setText(null);
        startScanContainerCodeEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(that, startScanContainerCodeEditText);
        scanEditTextTool.setComplete(this);

        startDetailContainerIdTextView.setText("托盘码:" + expensiveItem.getMarkContainerId());
        startDetailBoxNumTextView.setText("总箱数" + expensiveItem.getBoxNum());
        startDetailTurnOverBoxNumTextView.setText("总周转箱数:" + expensiveItem.getTurnoverBoxNum());
        startNextButton.setText(expensiveItem.isLoaded() ? "知道了" : "下一步");
        startNextButton.setEnabled(false);
    }


    private void popNextTujob() {
        checkContainerInfo();
        if (tuJobList != null && tuJobList.size() > 0) {
            fillTujobListLayout();
        } else {
            fillStartScanLayout();
        }
    }

    private void popNextExpensive() {
        checkContainerInfo();
        fillExpensiveListLayout();
    }

    private void checkContainerInfo() {
        if (containerInfo == null || !containerInfo.isLoaded()) {
            return;
        }
        if (tuJobList != null && tuJobList.size() > 0) {
            for (TuJobList.Item item : tuJobList.getTuJobList()) {
                if (TextUtils.equals(item.getContainerId(), containerInfo.getContainerId())) {
                    tuJobList.remove(item);
                    break;
                }
            }
        } else if (expensiveList != null && expensiveList.size() > 0) {
            for (ExpensiveList.Item item : expensiveList.getTuJobList()) {
                if (TextUtils.equals(item.getContainerId(), containerInfo.getContainerId())) {
                    expensiveList.remove(item);
                    break;
                }
            }

        }

    }

    private void requestContainerInfo(String markContainerId) {
        if (containerInfoTask != null) {
            containerInfoTask.cancel();
            containerInfoTask = null;
        }
        containerInfoTask = new ContainerInfoTask(that, markContainerId);
        containerInfoTask.start();
    }

    private void requestContainerSubmit(String containerId, String realContainerId, int taskBoardQty) {
        if (containerSubmitTask != null) {
            containerSubmitTask.cancel();
            containerSubmitTask = null;
        }
        containerSubmitTask = new ContainerSubmitTask(that, containerId, realContainerId, taskBoardQty);
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

    private void requestExpensiveList() {

        if (expensiveTask != null) {
            expensiveTask.cancel();
            expensiveTask = null;
        }
        expensiveTask = new ExpensiveTask(that);
        expensiveTask.start();
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
        if (startLayout.getVisibility() == View.VISIBLE) {
            if (tuJobList != null && tuJobList.size() > 0) {
                fillTujobListLayout();
                return;
            }
            if (expensiveList != null && expensiveList.size() > 0) {
                fillExpensiveListLayout();
                return;
            }
        }

        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (isItemClick) {
            return;
        }

        isItemClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isItemClick = false;
            }
        }, 500);

        if (v == tujobListFinishButton) {
            requestExpensiveList();
        } else if (v == expensiveListFinishButton) {
            requestConfirm();
        } else if (v == startFinishButton) {
            requestExpensiveList();
        } else if (v == startNextButton) {
            if (containerInfo != null) {
                if (!containerInfo.isLoaded()) {
                    Editable editable = startScanContainerCodeEditText.getText();
                    if (editable != null) {
                        requestContainerSubmit(containerInfo.getContainerId(), editable.toString(), containerInfo.getTaskBoardQty());
                    }
                } else {
                    if (expensiveList != null) {
                        popNextExpensive();
                    } else {
                        popNextTujob();
                    }
                }
            }
        } else if (v == tujobListSplitButton) {
            if (tuJobList != null) {
                tuJobList.clear();
            }
            fillStartScanLayout();
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
        Editable editable = startScanContainerCodeEditText.getText();
        if (editable != null) {
            requestContainerInfo(editable.toString());
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        requestContainerInfo(tuJobList.get(position).getMarkContainerId());
        if (tujobListLayout.getVisibility() == View.VISIBLE) {
            tujobItem = tuJobList.get(position);

            fillTuJobLayout();
        } else if (expensiveListView.getVisibility() == View.VISIBLE) {
            expensiveItem = expensiveList.get(position);

            fillExpensiveLayout();
        }

    }

    /**
     * 托盘码详情
     */
    private class ContainerInfoTask extends HttpAsyncTask<ContainerInfo> {

        private String markContainerId;

        public ContainerInfoTask(Context context, String markContainerId) {
            super(context, true, true, false);
            this.markContainerId = markContainerId;
        }

        @Override
        public DataHull<ContainerInfo> doInBackground() {
            return ContainerInfoProvier.request(context, uId, uToken, tu, markContainerId);
        }

        @Override
        public void onPostExecute(ContainerInfo result) {
            containerInfo = result;
            checkContainerInfo();

            if (expensiveList != null) {
                //贵品
                if (expensiveItem != null) {
                    if (!TextUtils.equals(expensiveItem.getContainerId(), result.getContainerId())) {
                        ToastTool.show(that, "托盘码不一致");
                        startNextButton.setEnabled(false);
                    } else {
                        startScanLayout.setVisibility(View.GONE);
                        if (scanEditTextTool != null) {
                            scanEditTextTool.release();
                            scanEditTextTool = null;
                        }
                        startNextButton.setEnabled(true);
                    }
                    return;
                }
            } else {
                //普通商品、尾货
                if (tujobItem != null) {
                    if (!TextUtils.equals(tujobItem.getContainerId(), result.getContainerId())) {
                        ToastTool.show(that, "托盘码不一致");
                        startNextButton.setEnabled(false);
                    } else {
                        startScanLayout.setVisibility(View.GONE);
                        if (scanEditTextTool != null) {
                            scanEditTextTool.release();
                            scanEditTextTool = null;
                        }
                        startNextButton.setEnabled(true);
                    }
                    return;
                }
                fillStartDetailLayout(markContainerId);
            }

        }
    }

    /**
     * 托盘码信息提交
     */
    private class ContainerSubmitTask extends HttpAsyncTask<ResponseState> {

        private String containerId;

        private String realContainerId;

        private int taskBoardQty;

        public ContainerSubmitTask(Context context, String containerId, String realContainerId, int taskBoardQty) {
            super(context, true, true, false);
            this.containerId = containerId;
            this.realContainerId = realContainerId;
            this.taskBoardQty = taskBoardQty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ContainerSubmitProvier.request(context, uId, uToken, tu, containerId, realContainerId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            containerInfo.setLoaded(true);
            if (expensiveList != null) {
                popNextExpensive();
            } else {
                popNextTujob();
            }

            if (taskBoardQty > 1) {
                DialogTools.showOneButtonDialog(that, "多板提交,请记得多板一起装车", "知道了", null, false);
            }
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
            that.setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 装车完毕
     */
    private class ExpensiveTask extends HttpAsyncTask<ExpensiveList> {

        public ExpensiveTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<ExpensiveList> doInBackground() {
            return ExpensiveListProvier.request(context, uId, uToken, tu);
        }

        @Override
        public void onPostExecute(final ExpensiveList result) {
            DialogTools.showTwoButtonDialog(that, "你有贵品列表,是否进行贵品装车?", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestConfirm();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    expensiveList = result;
                    fillExpensiveListLayout();
                }
            }, false);
        }


        @Override
        public void dataNull(String errMsg) {
            requestConfirm();
        }
    }
}
