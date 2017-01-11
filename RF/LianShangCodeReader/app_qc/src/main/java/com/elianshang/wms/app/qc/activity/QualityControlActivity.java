package com.elianshang.wms.app.qc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import com.elianshang.wms.app.qc.adapter.MyAdapter;
import com.elianshang.wms.app.qc.bean.QCDoneState;
import com.elianshang.wms.app.qc.bean.QcList;
import com.elianshang.wms.app.qc.bean.ResponseState;
import com.elianshang.wms.app.qc.provider.ConfirmProvider;
import com.elianshang.wms.app.qc.provider.DealCaseProvider;
import com.elianshang.wms.app.qc.provider.QCOneItemProvider;
import com.elianshang.wms.app.qc.provider.ScanProvider;
import com.elianshang.wms.app.qc.util.DataFormat;
import com.xue.http.impl.DataHull;

import java.util.Collections;
import java.util.Comparator;

public class QualityControlActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener, MyAdapter.OnItemClickListener {

    private String uId;

    private String uToken;

    /**
     * QC模式 0 流式 1 列表式
     */
    private int mode = 1;

    /**
     * 是否显示menu
     */
    private boolean showMenuItem = true;

    private Toolbar toolbar;

    private View checkProgressButton;

    /**
     * 扫描拣货ID布局
     */
    private View scanLayout;

    /**
     * 扫描拣货签EditText
     */
    private ScanEditText scanUnknownCodeEditText;

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

    private TextView startPickerNameTextView;

    /**
     * 开始布局集货道
     */
    private TextView startCollectionCodeTextView;

    /**
     * 开始布局超市me
     */
    private TextView startStoreNameTextView;

    private TextView startStoreNoTextView;

    private TextView startContainerIdTextView;

    /**
     * 开始布局总箱数
     */
    private TextView startAllBoxNumTextView;

    /**
     * 开始布局行数
     */
    private TextView startLineNumTextView;
    /**
     * 开始布局提交按钮
     */
    private Button startSubmitButton;
    /**
     * 开始布局 跳过按钮
     */
    private Button startSkipButton;

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

    private TextView itemPickerNameTextView;

    private TextView itemBarcodeTextView;

    private TextView itemSkuCodeTextView;

    private TextView itemPackCodeTextView;

    private TextView itemContainerIdTextView;

    /**
     * 规格文本框
     */
    private TextView itemPackNameTextView;

    /**
     * 数量文本框
     */
    private TextView itemQtyTextView;

    /**
     * 实际数量输入框
     */
    private QtyEditText itemInputQtyEditText;
    /**
     * 残次数量 view
     */
    private View itemShoddyView;
    /**
     * 残次数量输入框
     */
    private QtyEditText itemShoddyQtyEditView;

    /**
     * 提交按钮
     */
    private Button itemSubmitButton;
    /**
     * 复qc,修复异常
     */
    private View itemDealCaseView;
    /**
     * 复qc,忽略
     */
    private Button itemDealCaseButton1;
    /**
     * 复qc,回滚
     */
    private Button itemDealCaseButton2;
    /**
     * 复qc,修复
     */
    private Button itemDealCaseButton3;

    /**
     * 最终确认布局
     */
    private View confirmLayout;

    /**
     * 确认布局集货道
     */
    private TextView confirmCollectionCodeTextView;

    /**
     * 确认布局超市
     */
    private TextView confirmStoreNameTextView;

    private TextView confirmPickerNameTextView;

    private TextView confirmStoreNoTextView;

    private TextView confirmContainerIdTextView;

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

    private ListView listView;

    private MyAdapter myAdapter;

    private QcList qcList;

    private ScanEditTextTool scanEditTextTool;

    private QcList.Item curItem;

    private String serialNumber;

    private boolean isSkip = false;

    private boolean isItemClick = false;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qc_activity_main);
        if (readExtras()) {
            findView();
            fillScanLayout();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

//        uId = "1";
//        uToken = "25061134202027";
//        ScanManager.init(that);

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
        scanUnknownCodeEditText = (ScanEditText) scanLayout.findViewById(R.id.unknownCode_EditText);

        startLayout = findViewById(R.id.start_Layout);
        startTaskIdTextView = (TextView) startLayout.findViewById(R.id.qcTaskId_TextView);
        startStateTextView = (TextView) startLayout.findViewById(R.id.state_TextView);
        startCollectionCodeTextView = (TextView) startLayout.findViewById(R.id.collectionCode_TextView);
        startStoreNameTextView = (TextView) startLayout.findViewById(R.id.storeName_TextView);
        startStoreNoTextView = (TextView) startLayout.findViewById(R.id.storeNo_TextView);
        startContainerIdTextView = (TextView) startLayout.findViewById(R.id.containerId_TextView);
        startAllBoxNumTextView = (TextView) startLayout.findViewById(R.id.allBoxNum_TextView);
        startLineNumTextView = (TextView) startLayout.findViewById(R.id.lineNum_TextView);
        startSubmitButton = (Button) startLayout.findViewById(R.id.submit_Button);
        startSkipButton = (Button) startLayout.findViewById(R.id.skip_Button);
        startPickerNameTextView = (TextView) startLayout.findViewById(R.id.pickerName_TextView);
        startSubmitButton.setOnClickListener(this);
        startSkipButton.setOnClickListener(this);

        promptLayout = findViewById(R.id.prompt_Layout);

        itemLayout = findViewById(R.id.item_Layout);
        itemTaskIdTextView = (TextView) itemLayout.findViewById(R.id.qcTaskId_TextView);
        itemItemNameTextView = (TextView) itemLayout.findViewById(R.id.itemName_TextView);
        itemPickerNameTextView = (TextView) itemLayout.findViewById(R.id.pickerName_TextView);
        itemBarcodeTextView = (TextView) itemLayout.findViewById(R.id.barcode_TextView);
        itemSkuCodeTextView = (TextView) itemLayout.findViewById(R.id.skuCode_TextView);
        itemPackCodeTextView = (TextView) itemLayout.findViewById(R.id.packCode_TextView);
        itemContainerIdTextView = (TextView) itemLayout.findViewById(R.id.containerId_TextView);
        itemPackNameTextView = (TextView) itemLayout.findViewById(R.id.packName_TextView);
        itemQtyTextView = (TextView) itemLayout.findViewById(R.id.qty_TextView);
        itemInputQtyEditText = (QtyEditText) itemLayout.findViewById(R.id.inputQty_EditView);
        itemShoddyView = itemLayout.findViewById(R.id.shoddy_view);
        itemShoddyQtyEditView = (QtyEditText) itemLayout.findViewById(R.id.shoddyQty_EditView);
        itemSubmitButton = (Button) itemLayout.findViewById(R.id.submit_Button);
        itemDealCaseView = itemLayout.findViewById(R.id.dealCase);
        itemDealCaseButton1 = (Button) itemLayout.findViewById(R.id.dealCase_Button1);
        itemDealCaseButton2 = (Button) itemLayout.findViewById(R.id.dealCase_Button2);
        itemDealCaseButton3 = (Button) itemLayout.findViewById(R.id.dealCase_Button3);
        itemSubmitButton.setOnClickListener(this);
        itemDealCaseButton1.setOnClickListener(this);
        itemDealCaseButton2.setOnClickListener(this);
        itemDealCaseButton3.setOnClickListener(this);

        confirmLayout = findViewById(R.id.confirm_Layout);
        confirmCollectionCodeTextView = (TextView) confirmLayout.findViewById(R.id.collectionCode_TextView);
        confirmPickerNameTextView = (TextView) confirmLayout.findViewById(R.id.pickerName_TextView);
        confirmStoreNameTextView = (TextView) confirmLayout.findViewById(R.id.storeName_TextView);
        confirmStoreNoTextView = (TextView) confirmLayout.findViewById(R.id.storeNo_TextView);
        confirmContainerIdTextView = (TextView) confirmLayout.findViewById(R.id.containerId_TextView);
        confirmItemBoxNumTextView = (TextView) confirmLayout.findViewById(R.id.itemBoxNum_TextView);
        confirmTurnoverBoxNumTextView = (TextView) confirmLayout.findViewById(R.id.turnoverBoxNum_TextView);
        confirmItemBoxNumEditText = (QtyEditText) confirmLayout.findViewById(R.id.itemBoxNum_EditText);
        confirmTurnoverBoxNumEditText = (QtyEditText) confirmLayout.findViewById(R.id.turnoverBoxNum_EditText);
        confirmSubmitButton = (Button) confirmLayout.findViewById(R.id.submit_Button);
        listView = (ListView) findViewById(R.id.list_Layout);

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

    /**
     * 扫描页
     */
    private void fillScanLayout() {
        qcList = null;
        myAdapter = null;
        listView.setAdapter(null);

        scanUnknownCodeEditText.setText("");
        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanUnknownCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    /**
     * 开始详情页
     */
    private void fillStartLayout() {
        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.VISIBLE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        startTaskIdTextView.setText(qcList.getQcTaskId());
        startPickerNameTextView.setText(qcList.getPickerName());
        startStateTextView.setText(qcList.isFirst() ? "未完成" : (qcList.isQcDone() ? "完成" : "QC异常"));
        startCollectionCodeTextView.setText(qcList.getCollectionRoadCode());
        startStoreNameTextView.setText(qcList.getCustomerName());
        startStoreNoTextView.setText(qcList.getCustomerCode());
        startAllBoxNumTextView.setText(qcList.getAllBoxNum());
        startLineNumTextView.setText(qcList.getItemLineNum());
        startSubmitButton.setText(qcList.isQcDone() ? "查看" : "开始QC");
        startSkipButton.setVisibility(qcList.isFirst() ? View.VISIBLE : View.GONE);
        startContainerIdTextView.setText(qcList.getContainerId());

    }

    /**
     * 详情页
     */
    private void fillItemLayout(QcList.Item item) {
        if (item == null) {
            return;
        }
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        curItem = item;

        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.VISIBLE);
        confirmLayout.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        if (qcList.isFirst()) {
            itemShoddyView.setVisibility(View.VISIBLE);
            itemSubmitButton.setVisibility(View.VISIBLE);
            itemDealCaseView.setVisibility(View.GONE);
        } else {
            if (item.isQcDone()) {
                itemShoddyView.setVisibility(View.VISIBLE);
                itemSubmitButton.setVisibility(View.VISIBLE);
                itemDealCaseView.setVisibility(View.GONE);
            } else {
                itemShoddyView.setVisibility(View.GONE);
                itemSubmitButton.setVisibility(View.GONE);
                itemDealCaseView.setVisibility(View.VISIBLE);
            }
        }

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        itemTaskIdTextView.setText(qcList.getQcTaskId());
        itemItemNameTextView.setText(curItem.getItemName());
        itemPickerNameTextView.setText(qcList.getPickerName());
        itemBarcodeTextView.setText(curItem.getBarCode());
        itemSkuCodeTextView.setText(curItem.getSkuCode());
        itemPackCodeTextView.setText(curItem.getPackCode());
        itemPackNameTextView.setText(curItem.getPackName());
        itemQtyTextView.setText(curItem.getUomQty());
        itemInputQtyEditText.setText(null);
        itemInputQtyEditText.setHint(curItem.getUomQty());
        itemShoddyQtyEditView.setText(null);
        itemShoddyQtyEditView.setHint("0");
        itemContainerIdTextView.setText(qcList.getContainerId());

    }

    /**
     * 列表页
     */
    private void fillListLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        if (listView.getAdapter() == null) {
            myAdapter = new MyAdapter(that);
            myAdapter.setOnItemClickListener(this);
            listView.setAdapter(myAdapter);
        }
        myAdapter.setQcList(getSortedQcList());
        myAdapter.notifyDataSetChanged();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
    }

    /**
     * 确认页
     */
    private void fillConfirmLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        checkProgressButton.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        promptLayout.setVisibility(View.GONE);
        itemLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        confirmCollectionCodeTextView.setText(qcList.getCollectionRoadCode());
        confirmPickerNameTextView.setText(qcList.getPickerName());
        confirmStoreNameTextView.setText(qcList.getCustomerName());
        confirmStoreNoTextView.setText(qcList.getCustomerCode());
        confirmItemBoxNumTextView.setText(qcList.getItemBoxNum());
        confirmTurnoverBoxNumTextView.setText(qcList.getTurnoverBoxNum());
        confirmItemBoxNumEditText.setText(null);
        confirmTurnoverBoxNumEditText.setText(null);
        confirmContainerIdTextView.setText(qcList.getContainerId());
    }

    /**
     * 根据请求接口任务
     */
    private void popItem(String barCode) {
        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(barCode, item.getBarCode())) {
                fillItemLayout(item);
                return;
            }
        }
    }

    /**
     * 提交完成后，进行下一个任务的判断逻辑
     */
    private void popNextItem(String barCode, String uomQty, String defectQty, boolean qcDone) {
        if (qcList.isQcDone()) {
            fillListLayout();
            return;
        }

        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(barCode, item.getBarCode())) {//QC过了的,就改变下状态
                item.setFirst(false);
                item.setQcDone(TextUtils.equals(DataFormat.getFormatValue(item.getUomQty()), uomQty) && (TextUtils.equals(defectQty, "0")));
            }
        }

        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (qcList.isFirst()) {//首次qc 检查是否qc
                if (item.isFirst()) {
                    showMenuItem = false;
                    fillListLayout();
                    return;
                }
            } else {//复qc 检查qc异常
                if (!item.isQcDone()) {
                    showMenuItem = false;
                    fillListLayout();
                    return;
                }
            }
        }

        checkConfirm(qcDone);
    }

    /**
     * 更具 箱码 ， 国条 弹出任务
     */
    private void findItem(String code) {
        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(code, item.getPackCode())) {
                fillItemLayout(item);
                return;
            }
        }

        for (int i = 0; i < qcList.size(); i++) {
            QcList.Item item = qcList.get(i);
            if (TextUtils.equals(code, item.getBarCode())) {
                fillItemLayout(item);
                return;
            }
        }

        ToastTool.show(that, "商品不在拣货列表内");
    }

    /**
     * 任务本地结束判断
     */
    private void checkConfirm(boolean qcDone) {
        if (qcDone) {
            fillConfirmLayout();
        } else {
            DialogTools.showOneButtonDialog(that, "QC 结果异常，阶段性 QC 完毕！", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fillScanLayout();
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
            if (!item.isFirst()) {
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

    private QcList getSortedQcList() {
        QcList sortedQcList = new QcList();
        sortedQcList.addAll(qcList);
        if (!qcList.isFirst()) {//qc完成,按照是否异常优先级排序
            Collections.sort(sortedQcList, new Comparator<QcList.Item>() {
                @Override
                public int compare(QcList.Item lhs, QcList.Item rhs) {
                    int lhsPriority = 0;
                    int rhsPriority = 0;

                    if (lhs.isQcDone()) {
                        lhsPriority += 4;
                    }

                    if (lhs.isSplit()) {
                        lhsPriority += 2;
                    }

                    if (rhs.isQcDone()) {
                        rhsPriority += 4;
                    }
                    if (rhs.isSplit()) {
                        rhsPriority += 2;
                    }
                    return lhsPriority - rhsPriority;
                }
            });
        } else {//未qc,按照是否qc优先级排序
            Collections.sort(sortedQcList, new Comparator<QcList.Item>() {
                @Override
                public int compare(QcList.Item lhs, QcList.Item rhs) {
                    int lhsPriority = 0;
                    int rhsPriority = 0;
                    if (!lhs.isFirst()) {
                        lhsPriority += 4;
                    }

                    if (lhs.isSplit()) {
                        lhsPriority += 2;
                    }

                    if (!rhs.isFirst()) {
                        rhsPriority += 4;
                    }

                    if (rhs.isSplit()) {
                        rhsPriority += 2;
                    }
                    return lhsPriority - rhsPriority;
                }
            });
        }

        return sortedQcList;
    }

    @Override
    public void onBackPressed() {
        if (qcList != null) {
            if (qcList.isQcDone() && listView.getVisibility() == View.VISIBLE) {
                fillStartLayout();
                return;
            }

            if (qcList.isQcDone() && startLayout.getVisibility() == View.VISIBLE) {
                fillScanLayout();
                return;
            }

            if (mode == 1 && (promptLayout.getVisibility() == View.VISIBLE || itemLayout.getVisibility() == View.VISIBLE)) {
                fillListLayout();
                return;
            }
            if (isSkip && confirmLayout.getVisibility() == View.VISIBLE) {//开始qc页面,点击跳过进入confirm页,点击返回开始qc页。
                isSkip = false;
                fillStartLayout();
                return;
            }
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

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            if (scanEditTextTool == null) {
                return;
            }

            scanEditTextTool.setScanText(s);
        } else if (listView.getVisibility() == View.VISIBLE) {
            findItem(s);
        }
    }

    @Override
    public void onComplete() {
        String unknownCode = scanUnknownCodeEditText.getText().toString();
        new ScanTask(that, unknownCode).start();
    }

    @Override
    public void onError(ContentEditText editText) {

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

        if (v == checkProgressButton) {
            checkProgress();
        } else if (v == itemSubmitButton) {
            String qcTaskId = qcList.getQcTaskId();
            String code = curItem.getBarCode();
            String uomQty = itemInputQtyEditText.getValue();
            String defectQty = itemShoddyQtyEditView.getValue();
            if (TextUtils.isEmpty(uomQty) && TextUtils.isEmpty(defectQty)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }
            new QcOneTask(that, qcTaskId, code, uomQty, defectQty).start();
        } else if (v == itemDealCaseButton1) {
            String containerId = qcList.getContainerId();
            String code = curItem.getBarCode();
            String uomQty = itemInputQtyEditText.getValue();
            if (TextUtils.isEmpty(uomQty)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }
            new DealCaseTask(that, containerId, code, uomQty, 1).start();
        } else if (v == itemDealCaseButton2) {
            String containerId = qcList.getContainerId();
            String code = curItem.getBarCode();
            String uomQty = itemInputQtyEditText.getValue();
            if (TextUtils.isEmpty(uomQty)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }
            new DealCaseTask(that, containerId, code, uomQty, 2).start();
        } else if (v == itemDealCaseButton3) {
            String containerId = qcList.getContainerId();
            String code = curItem.getBarCode();
            String uomQty = itemInputQtyEditText.getValue();
            if (TextUtils.isEmpty(uomQty)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }
            new DealCaseTask(that, containerId, code, uomQty, 3).start();
        } else if (v == startSubmitButton) {
            boolean isFirst = qcList.isFirst();
            if (!isFirst) {//已qc---只显示列表
                mode = 1;
                showMenuItem = false;
            }
            popNextItem(null, null, null, true);
        } else if (v == startSkipButton) {
            isSkip = true;
            fillConfirmLayout();
        } else if (v == confirmSubmitButton) {
            String itemBoxNum = confirmItemBoxNumEditText.getValue();
            String turnoverBoxNum = confirmTurnoverBoxNumEditText.getValue();
            if (TextUtils.isEmpty(itemBoxNum) && TextUtils.isEmpty(turnoverBoxNum)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }
            new ConfirmTask(that, qcList.getQcTaskId(), itemBoxNum, turnoverBoxNum).start();
        }
    }

    @Override
    public void onItemClick(String barCode) {
        if (qcList.isQcDone()) {
            return;
        }
        popItem(barCode);
    }

    private class ScanTask extends HttpAsyncTask<QcList> {

        private String unknownCode;

        public ScanTask(Context context, String unknownCode) {
            super(context, true, true);

            this.unknownCode = unknownCode;
        }

        @Override
        public DataHull<QcList> doInBackground() {
            return ScanProvider.request(context, uId, uToken, unknownCode);
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
            curItem = null;
            popNextItem(code, uomQty, defectQty, result.isDone());
        }
    }

    private class DealCaseTask extends HttpAsyncTask<QCDoneState> {

        private String containerId;

        private String code;

        private String uomQty;

        private int type;

        public DealCaseTask(Context context, String containerId, String code, String uomQty, int type) {
            super(context, true, true);

            this.containerId = containerId;
            this.code = code;
            this.uomQty = uomQty;
            this.type = type;
        }

        @Override
        public DataHull<QCDoneState> doInBackground() {
            return DealCaseProvider.request(context, uId, uToken, containerId, code, uomQty, String.valueOf(type), serialNumber);
        }

        @Override
        public void onPostExecute(QCDoneState result) {
            curItem = null;
            popNextItem(code, uomQty, "0", result.isDone());
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
            return ConfirmProvider.request(context, uId, uToken, qcTaskId, boxNum, turnoverBoxNum, isSkip, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillScanLayout();
            ToastTool.show(context, "QC完成");
        }
    }
}