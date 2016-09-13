package com.elianshang.wms.app.qc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.qc.R;
import com.elianshang.wms.app.qc.bean.QCDoneState;
import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.bean.ResponseState;
import com.elianshang.wms.app.qc.provider.ConfirmProvider;
import com.elianshang.wms.app.qc.provider.QCOneItemProvider;
import com.elianshang.wms.app.qc.provider.ScanProvider;
import com.xue.http.impl.DataHull;


public class QualityControlActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View checkProgressButton;

    /**
     * 扫描拣货ID布局
     */
    private View scanLayout;

    /**
     * 扫描拣货签EditText
     */
    private ScanEditText scanPickTaskIdEditText;

    /**
     * 开始任务布局
     */
    private View startLayout;

    /**
     * 开始布局任务
     */
    private TextView startTaskIdTextView;

    /**
     * 开始布局状态
     */
    private TextView startStateTextView;

    /**
     * 开始布局集货道
     */
    private TextView startCollectionCodeTextView;

    /**
     * 开始布局超市
     */
    private TextView startShopTextView;

    /**
     * 开始布局总箱数
     */
    private TextView startAllBoxNumTextView;

    /**
     * 开始布局行数
     */
    private TextView startLineNumTextView;

    /**
     * 扫描提示布局
     */
    private View promptLayout;

    /**
     * 商品详情布局
     */
    private View itemLayout;

    /**
     * 任务文本框
     */
    private TextView itemTaskIdTextView;

    /**
     * 名称文本框
     */
    private TextView itemItemNameTextView;

    /**
     * 规格文本框
     */
    private TextView itemPackNameTextView;

    /**
     * 数量文本框
     */
    private TextView itemQtyTextView;

    /**
     * 开始布局提交按钮
     */
    private Button startSubmitButton;

    /**
     * 实际数量输入框
     */
    private QtyEditText itemInputQtyEditText;

    /**
     * 残次数量输入框
     */
    private QtyEditText itemShoddyQtyEditView;

    /**
     * 提交按钮
     */
    private Button itemSubmitButton;

    /**
     * 最终确认布局
     */
    private View confirmLayout;

    /**
     * 确认布局任务
     */
    private TextView confirmPickTaskIdTextView;

    /**
     * 确认布局集货道
     */
    private TextView confirmCollectionCodeTextView;

    /**
     * 确认布局超市
     */
    private TextView confirmShopTextView;

    /**
     * 确认布局箱数
     */
    private TextView confirmItemBoxNumTextView;

    /**
     * 确认布局周转箱数
     */
    private TextView confirmTurnoverBoxNumTextView;

    /**
     * 确认布局箱数
     */
    private QtyEditText confirmItemBoxNumEditText;

    /**
     * 确认布局周转箱数
     */
    private QtyEditText confirmTurnoverBoxNumEditText;

    /**
     * 确认提交按钮
     */
    private Button confirmSubmitButton;

    private QcList qcList;

    private ScanEditTextTool scanEditTextTool;

    private QcList.Item curItem;

    private String serialNumber;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualitycontrol);
        if (readExtras()) {
            findView();
            fillScanLayout();
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

    private void findView() {
        initToolbar();

        checkProgressButton = findViewById(R.id.check_progress);
        checkProgressButton.setOnClickListener(this);

        scanLayout = findViewById(R.id.scan_Layout);
        scanPickTaskIdEditText = (ScanEditText) scanLayout.findViewById(R.id.pickTaskId_EditText);

        startLayout = findViewById(R.id.start_Layout);
        startTaskIdTextView = (TextView) startLayout.findViewById(R.id.qcTaskId_TextView);
        startStateTextView = (TextView) startLayout.findViewById(R.id.state_TextView);
        startCollectionCodeTextView = (TextView) startLayout.findViewById(R.id.collectionCode_TextView);
        startShopTextView = (TextView) startLayout.findViewById(R.id.shop_TextView);
        startAllBoxNumTextView = (TextView) startLayout.findViewById(R.id.allBoxNum_TextView);
        startLineNumTextView = (TextView) startLayout.findViewById(R.id.lineNum_TextView);
        startSubmitButton = (Button) startLayout.findViewById(R.id.submit_Button);
        startSubmitButton.setOnClickListener(this);

        promptLayout = findViewById(R.id.prompt_Layout);

        itemLayout = findViewById(R.id.item_Layout);
        itemTaskIdTextView = (TextView) itemLayout.findViewById(R.id.qcTaskId_TextView);
        itemItemNameTextView = (TextView) itemLayout.findViewById(R.id.itemName_TextView);
        itemPackNameTextView = (TextView) itemLayout.findViewById(R.id.packName_TextView);
        itemQtyTextView = (TextView) itemLayout.findViewById(R.id.qty_TextView);
        itemInputQtyEditText = (QtyEditText) itemLayout.findViewById(R.id.inputQty_EditView);
        itemShoddyQtyEditView = (QtyEditText) itemLayout.findViewById(R.id.shoddyQty_EditView);
        itemSubmitButton = (Button) itemLayout.findViewById(R.id.submit_Button);
        itemSubmitButton.setOnClickListener(this);

        confirmLayout = findViewById(R.id.confirm_Layout);
        confirmPickTaskIdTextView = (TextView) confirmLayout.findViewById(R.id.pickTaskId_TextView);
        confirmCollectionCodeTextView = (TextView) confirmLayout.findViewById(R.id.collectionCode_TextView);
        confirmShopTextView = (TextView) confirmLayout.findViewById(R.id.shop_TextView);
        confirmItemBoxNumTextView = (TextView) confirmLayout.findViewById(R.id.itemBoxNum_TextView);
        confirmTurnoverBoxNumTextView = (TextView) confirmLayout.findViewById(R.id.turnoverBoxNum_TextView);
        confirmItemBoxNumEditText = (QtyEditText) confirmLayout.findViewById(R.id.itemBoxNum_EditText);
        confirmTurnoverBoxNumEditText = (QtyEditText) confirmLayout.findViewById(R.id.turnoverBoxNum_EditText);
        confirmSubmitButton = (Button) confirmLayout.findViewById(R.id.submit_Button);

        confirmSubmitButton.setOnClickListener(this);
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

    private void fillScanLayout() {
        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanPickTaskIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillStartLayout() {
        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        startTaskIdTextView.setText(qcList.getQcTaskId());
        startStateTextView.setText(qcList.isQcTaskDone() ? "完成" : "未完成");
        startCollectionCodeTextView.setText(qcList.getCollectionRoadCode());
        startShopTextView.setText(qcList.getCustomerName());
        startAllBoxNumTextView.setText(qcList.getAllBoxNum());
        startLineNumTextView.setText(qcList.getItemLineNum());
        startSubmitButton.setText(qcList.isQcTaskDone() ? "退出" : "开始QC");
    }

    private void fillPromptLayout() {
        checkProgressButton.setVisibility(View.VISIBLE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.VISIBLE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
    }

    private void fillItemLayout(QcList.Item item) {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        curItem = item;

        checkProgressButton.setVisibility(View.VISIBLE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.VISIBLE);
        confirmLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        itemTaskIdTextView.setText(qcList.getQcTaskId());
        itemItemNameTextView.setText(curItem.getItemName());
        itemPackNameTextView.setText(curItem.getPackName());
        itemQtyTextView.setText(curItem.getUomQty());
        itemInputQtyEditText.setHint(curItem.getUomQty());
        itemInputQtyEditText.setText(null);
        itemShoddyQtyEditView.setHint("0");
        itemShoddyQtyEditView.setText(null);
    }

    private void fillConfirmLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        confirmPickTaskIdTextView.setText(qcList.getPickTaskId());
        confirmCollectionCodeTextView.setText(qcList.getCollectionRoadCode());
        confirmShopTextView.setText(qcList.getCustomerName());
        confirmItemBoxNumTextView.setText(qcList.getItemBoxNum());
        confirmTurnoverBoxNumTextView.setText(qcList.getTurnoverBoxNum());
        confirmItemBoxNumEditText.setText(null);
        confirmItemBoxNumEditText.setHint(qcList.getItemBoxNum());
        confirmTurnoverBoxNumEditText.setText(null);
        confirmTurnoverBoxNumEditText.setHint(qcList.getTurnoverBoxNum());
    }

    /**
     * 根据请求接口找下一个任务
     */
    private void popNextItem(String barCode, boolean qcDone) {
        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(barCode, item.getBarCode())) {//QC过了的,就改变下状态
                item.setQcDone(true);
            }
        }

        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (!item.isQcDone()) {
                if (item.isSplit()) {
                    fillPromptLayout();
                } else {
                    fillItemLayout(item);
                }

                return;
            }
        }

        checkConfirm(qcDone);
    }

    /**
     * 扫描找拆零的任务
     */
    private void findItem(String barCode) {
        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(barCode, item.getBarCode())) {
                if (item.isQcDone()) {
                    ToastTool.show(that, "该商品已经QC过了,不要重复QC");
                } else {
                    fillItemLayout(item);
                    return;
                }
            }
        }

        ToastTool.show(that, "商品不在拣货列表内");
    }

    private void checkConfirm(boolean qcDone) {
        if (qcDone) {
            fillConfirmLayout();
        } else {
            DialogTools.showOneButtonDialog(that, "QC 结果异常，阶段性 QC 完毕！", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, false);
        }
    }

    protected void checkProgress() {
        if (qcList == null) {
            return;
        }
        QcList missList = new QcList();
        for (QcList.Item item : qcList) {
            if (!item.isQcDone()) {
                missList.add(item);
            }
        }

        if (missList.size() > 0) {
            String msg = "未QC的商品列表:\n";

            for (QcList.Item item : missList) {
                msg += item.getItemName();
                msg += "  ";
                msg += item.getPackName();
                msg += " * ";
                msg += item.getUomQty();
                msg += "\n";
            }

            DialogTools.showOneButtonDialog(that, msg, "知道了", null, true);
            return;
        } else {
            DialogTools.showOneButtonDialog(that, "已经没有未QC商品", "知道了", null, true);
        }
    }

    @Override
    public void onBackPressed() {
        if (qcList != null) {
            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        } else {
            super.onBackPressed();
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
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            if (scanEditTextTool == null) {
                return;
            }

            scanEditTextTool.setScanText(s);
        } else if (promptLayout.getVisibility() == View.VISIBLE) {
            findItem(s);
        }
    }

    @Override
    public void onComplete() {
        String pickTaskId = scanPickTaskIdEditText.getText().toString();
        new ScanTask(that, pickTaskId).start();
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void onClick(View v) {
        if (v == checkProgressButton) {
            checkProgress();
        } else if (v == itemSubmitButton) {
            String qcTaskId = qcList.getQcTaskId();
            String code = curItem.getBarCode();
            String uomQty = itemInputQtyEditText.getValue();
            String defectQty = itemShoddyQtyEditView.getValue();

            new QcOneTask(that, qcTaskId, code, uomQty, defectQty).start();
        } else if (v == startSubmitButton) {
            if (qcList.isQcTaskDone()) {
                finish();
            } else {
                popNextItem(null, false);
            }
        } else if (v == confirmSubmitButton) {
            String itemBoxNum = confirmItemBoxNumEditText.getValue();
            String turnoverBoxNum = confirmTurnoverBoxNumEditText.getValue();

            new ConfirmTask(that, qcList.getQcTaskId(), itemBoxNum, turnoverBoxNum).start();
        }
    }

    private class ScanTask extends HttpAsyncTask<QcList> {

        private String pickTaskId;

        public ScanTask(Context context, String pickTaskId) {
            super(context, true, true);

            this.pickTaskId = pickTaskId;
        }

        @Override
        public DataHull<QcList> doInBackground() {
            return ScanProvider.request(context, uId, uToken, pickTaskId);
        }

        @Override
        public void onPostExecute(QcList result) {
            qcList = result;
            fillStartLayout();
        }
    }

    private class QcOneTask extends HttpAsyncTask<QCDoneState> {

        private String qcTaskId;

        private String code;

        private String uomQty;

        private String defectQty;

        public QcOneTask(Context context, String qcTaskId, String code, String uomQty, String defectQty) {
            super(context, true, true);

            this.qcTaskId = qcTaskId;
            this.code = code;
            this.uomQty = uomQty;
            this.defectQty = defectQty;
        }

        @Override
        public DataHull<QCDoneState> doInBackground() {
            return QCOneItemProvider.request(context, uId, uToken, qcTaskId, code, uomQty, defectQty, serialNumber);
        }

        @Override
        public void onPostExecute(QCDoneState result) {
            popNextItem(code, result.isDone());
        }
    }

    private class ConfirmTask extends HttpAsyncTask<ResponseState> {

        private String qcTaskId;

        private String boxNum;

        private String turnoverBoxNum;

        public ConfirmTask(Context context, String qcTaskId, String boxNum, String turnoverBoxNum) {
            super(context, true, true);

            this.qcTaskId = qcTaskId;
            this.boxNum = boxNum;
            this.turnoverBoxNum = turnoverBoxNum;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ConfirmProvider.request(context, uId, uToken, qcTaskId, boxNum, turnoverBoxNum, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            finish();
            ToastTool.show(context, "QC完成");
        }
    }
}