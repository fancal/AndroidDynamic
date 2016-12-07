package com.elianshang.wms.app.mergeboard.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.mergeboard.R;
import com.elianshang.wms.app.mergeboard.bean.CheckMerge;
import com.elianshang.wms.app.mergeboard.bean.ResponseState;
import com.elianshang.wms.app.mergeboard.provider.CheckMergeContainersProvider;
import com.elianshang.wms.app.mergeboard.provider.MergeContainersProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class MergeBoardActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    /**
     * 添加托盘布局
     */
    private View inputView;

    /**
     * 添加托盘布局 item容器
     */
    private LinearLayout inputLayout;

    private TextView titleTextView;

    /**
     * 详情布局
     */
    private View detailView;

    /**
     * 详情布局 item容器
     */
    private LinearLayout detailLayout;

    private CheckBox oneCheckBox;

    private CheckBox twoCheckBox;

    /**
     * 提交
     */
    private Button submitButton;

    /**
     * 动态布局记录列表
     */
    private ArrayList<ViewHolder> vhList = new ArrayList<>();

    private String serialNumber;
    /**
     * 最大允许合板数
     */
    private final int MAX_BOARD_NUM = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mergeboard_activity_main);

        if (readExtra()) {
            findView();
            fillInitBoard();
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

    private void findView() {

        inputView = findViewById(R.id.container_input);
        inputLayout = (LinearLayout) inputView.findViewById(R.id.input_Layout);
        titleTextView = (TextView) inputView.findViewById(R.id.title_TextView);
        detailView = findViewById(R.id.container_detail);
        detailLayout = (LinearLayout) detailView.findViewById(R.id.detail_Layout);
        submitButton = (Button) findViewById(R.id.submit_Button);

        submitButton.setOnClickListener(this);

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

        //FIXME
//        uId = "141871359725260";
//        uToken = "25061134202027";
//        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillInitBoard() {
        inputView.setVisibility(View.VISIBLE);
        detailView.setVisibility(View.GONE);

        vhList.clear();
        inputLayout.removeAllViews();
        titleTextView.setText("请扫描添加托盘码:");
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
    }

    private void fillBoardDetail(CheckMerge checkMerge) {
        if (vhList.size() <= 1) {
            submitButton.setVisibility(View.GONE);
        } else {
            submitButton.setVisibility(View.VISIBLE);
        }

        detailLayout.removeAllViews();

        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        inputView.setVisibility(View.GONE);
        detailView.setVisibility(View.VISIBLE);

        addDetailHeadView(checkMerge);

        for (CheckMerge.Item item : checkMerge) {
            addDetailItemView(item);
        }
    }

    private void addItemView(String containerId) {
        if (vhList.size() >= MAX_BOARD_NUM) {
            ToastTool.show(that, "超出最大合板数");
            return;
        }
        //允许托盘码相同
//        for (ViewHolder viewHolder : vhList) {
//            if (TextUtils.equals(containerId, viewHolder.containerIdEditText.getText().toString())) {
//                ToastTool.show(that, "该托盘码已经存在");
//                return;
//            }
//        }

        final View view = View.inflate(that, R.layout.mergeboard_input_item_view, null);

        ScanEditText containerIdEditText = (ScanEditText) view.findViewById(R.id.containerId_EditText);
        TextView deleteTextView = (TextView) view.findViewById(R.id.delete_TextView);

        inputLayout.addView(view);
        containerIdEditText.setText(containerId);

        final ViewHolder vh = new ViewHolder();
        vh.layout = view;
        vh.containerIdEditText = containerIdEditText;
        vh.deleteTextView = deleteTextView;
        vhList.add(vh);

        titleTextView.setText("托盘数:" + vhList.size());

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemView(vh);
            }
        });

        if (vhList.size() < 1) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
        }
    }

    private void removeItemView(ViewHolder viewHolder) {
        inputLayout.removeView(viewHolder.layout);
        vhList.remove(viewHolder);

        if (vhList.size() == 0) {
            titleTextView.setText("请扫描添加托盘码:");
        } else {
            titleTextView.setText("托盘数:" + vhList.size());
        }

        if (vhList.size() < 1) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
        }
    }

    private void addDetailHeadView(CheckMerge checkMerge) {

        View view = View.inflate(that, R.layout.mergeboard_detail_head_view, null);

        TextView storeNameTextView = (TextView) view.findViewById(R.id.storeName_TextView);
        TextView storeNoTextView = (TextView) view.findViewById(R.id.storeNo_TextView);
        TextView containerCountTextView = (TextView) view.findViewById(R.id.containerCount_TextView);
        TextView packCountTextView = (TextView) view.findViewById(R.id.packCount_TextView);
        TextView turnoverBoxCountTextView = (TextView) view.findViewById(R.id.turnoverBoxCount_TextView);
        CheckBox oneCheckBox = (CheckBox) view.findViewById(R.id.one_CheckBox);
        CheckBox twoCheckBox = (CheckBox) view.findViewById(R.id.two_CheckBox);

        storeNameTextView.setText(checkMerge.getCustomerName());
        storeNoTextView.setText(checkMerge.getCustomerCode());
        containerCountTextView.setText(checkMerge.getContainerCount());
        packCountTextView.setText(checkMerge.getPackCount());
        turnoverBoxCountTextView.setText(checkMerge.getTurnoverBoxCount());
        oneCheckBox.setChecked(true);
        twoCheckBox.setChecked(false);
        oneCheckBox.setOnClickListener(this);
        twoCheckBox.setOnClickListener(this);

        detailLayout.addView(view);
        this.oneCheckBox = oneCheckBox;
        this.twoCheckBox = twoCheckBox;
    }

    private void addDetailItemView(CheckMerge.Item item) {

        View view = View.inflate(that, R.layout.mergeboard_detail_item_view, null);

        TextView containerIdTextView = (TextView) view.findViewById(R.id.containerId_TextView);
        TextView packCountTextView = (TextView) view.findViewById(R.id.packCount_TextView);
        TextView turnoverBoxCountTextView = (TextView) view.findViewById(R.id.turnoverBoxCount_TextView);
        TextView statusTextView = (TextView) view.findViewById(R.id.status_TextView);

        containerIdTextView.setText(item.getContainerId());
        packCountTextView.setText(item.getPackCount());
        turnoverBoxCountTextView.setText(item.getTurnoverBoxCount());

        if (item.isMerged()) {
            statusTextView.setTextColor(0xffff0000);
            statusTextView.setText("已合板");
        } else {
            statusTextView.setTextColor(0xff00ee44);
            statusTextView.setText("未合板");
        }

        detailLayout.addView(view);
    }


    @Override
    public void onBackPressed() {
        if (detailView.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(that, "是否放弃合板合板", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputView.setVisibility(View.VISIBLE);
                    detailView.setVisibility(View.GONE);
                }
            }, true);

            return;
        }

        finish();
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (inputView.getVisibility() == View.VISIBLE) {
            addItemView(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (submitButton == v) {
            if (inputView.getVisibility() == View.VISIBLE) {
                new CheckMergeContainersTask(that, getBoardListString()).start();
            } else {
                String taskBoardQty = "1";
                if (twoCheckBox.isChecked()) {
                    taskBoardQty = "2";
                }
                new MergeContainersTask(that, getBoardListString(), taskBoardQty).start();
            }
        } else if (v == oneCheckBox) {
            oneCheckBox.setChecked(true);
            twoCheckBox.setChecked(false);
        } else if (v == twoCheckBox) {
            oneCheckBox.setChecked(false);
            twoCheckBox.setChecked(true);
        }
    }

    private String getBoardListString() {
        final JSONArray jsonarray = new JSONArray();

        for (ViewHolder vh : vhList) {
            String containerId = vh.containerIdEditText.getText().toString();
            if (!TextUtils.isEmpty(containerId)) {
                jsonarray.put(containerId);
            }
        }

        return jsonarray.toString();
    }

    private class ViewHolder {
        View layout;
        ScanEditText containerIdEditText;
        TextView deleteTextView;
    }

    /**
     * 拖板列表详情
     */
    private class CheckMergeContainersTask extends HttpAsyncTask<CheckMerge> {

        private String resultList;

        private CheckMergeContainersTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<CheckMerge> doInBackground() {
            return CheckMergeContainersProvider.request(context, uId, uToken, resultList);
        }

        @Override
        public void onPostExecute(CheckMerge result) {
            fillBoardDetail(result);
        }
    }

    /**
     * 合板
     */
    private class MergeContainersTask extends HttpAsyncTask<ResponseState> {

        private String resultList;

        private String taskBoardQty;

        private MergeContainersTask(Context context, String resultList, String taskBoardQty) {
            super(context, true, true);
            this.resultList = resultList;
            this.taskBoardQty = taskBoardQty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return MergeContainersProvider.request(context, uId, uToken, resultList, taskBoardQty, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillInitBoard();
            ToastTool.show(context, "合板完成");
        }
    }
}