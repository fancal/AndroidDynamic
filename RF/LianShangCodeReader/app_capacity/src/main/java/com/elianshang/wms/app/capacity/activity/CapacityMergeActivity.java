package com.elianshang.wms.app.capacity.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.capacity.R;
import com.elianshang.wms.app.capacity.bean.MergeBean;
import com.elianshang.wms.app.capacity.provider.MergeProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class CapacityMergeActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uId, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), CapacityMergeActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivityForResult(intent, 1);
    }

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

    private boolean isItemClick = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capacity_merge_activity_main);

        if (readExtra()) {
            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

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

        inputView = findViewById(R.id.input_View);
        inputLayout = (LinearLayout) inputView.findViewById(R.id.input_Layout);
        titleTextView = (TextView) inputView.findViewById(R.id.title_TextView);
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

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillInitBoard() {
        inputView.setVisibility(View.VISIBLE);

        vhList.clear();
        inputLayout.removeAllViews();
        titleTextView.setText("请扫描库位:");
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
    }

    private void addItemView(String locationCode) {
        if (vhList.size() >= MAX_BOARD_NUM) {
            ToastTool.show(that, "超出最大合并数");
            return;
        }
        //允许托盘码相同
        for (ViewHolder viewHolder : vhList) {
            if (TextUtils.equals(locationCode, viewHolder.locationCodeEditText.getText().toString())) {
                ToastTool.show(that, "该库位已经存在");
                return;
            }
        }

        final View view = View.inflate(that, R.layout.input_item_view, null);

        ScanEditText locationCodeEditText = (ScanEditText) view.findViewById(R.id.locationCode_EditText);
        TextView deleteTextView = (TextView) view.findViewById(R.id.delete_TextView);

        inputLayout.addView(view);
        locationCodeEditText.setText(locationCode);

        final ViewHolder vh = new ViewHolder();
        vh.locationCode = locationCode;
        vh.layout = view;
        vh.locationCodeEditText = locationCodeEditText;
        vh.deleteTextView = deleteTextView;
        vhList.add(vh);

        titleTextView.setText("库位数:" + vhList.size());

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemView(vh);
            }
        });

        if (vhList.size() < 2) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
        }
    }

    private void removeItemView(ViewHolder viewHolder) {
        inputLayout.removeView(viewHolder.layout);
        vhList.remove(viewHolder);

        if (vhList.size() == 0) {
            titleTextView.setText("请扫描添加库位:");
        } else {
            titleTextView.setText("库位数:" + vhList.size());
        }

        if (vhList.size() < 2) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
        }
    }

    private void fillMerge(MergeBean mergeBean) {
        if (mergeBean != null) {
            if (mergeBean.isDone()) {
                ToastTool.show(that, "库位合并成功");
                finish();
            } else {
                for (ViewHolder viewHolder : vhList) {
                    if (mergeBean.getErrCodes().contains(viewHolder.locationCode)) {
                        viewHolder.locationCodeEditText.setTextColor(0xffff0000);
                    }
                }
                ToastTool.show(that, mergeBean.getMsg());
            }
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        addItemView(s);
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

        if (submitButton == v) {
            new CheckMergeTask(that, getBoardListString()).start();
        }
    }

    private String getBoardListString() {
        final JSONArray jsonarray = new JSONArray();

        for (ViewHolder vh : vhList) {
            if (!TextUtils.isEmpty(vh.locationCode)) {
                jsonarray.put(vh.locationCode);
            }
        }

        return jsonarray.toString();
    }

    private class ViewHolder {

        String locationCode;

        View layout;

        ScanEditText locationCodeEditText;

        TextView deleteTextView;
    }

    /**
     * 拖板列表详情
     */
    private class CheckMergeTask extends HttpAsyncTask<MergeBean> {

        private String resultList;

        private CheckMergeTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<MergeBean> doInBackground() {
            return MergeProvider.request(context, uId, uToken, resultList, serialNumber);
        }

        @Override
        public void onPostExecute(MergeBean result) {
            fillMerge(result);
        }
    }
}