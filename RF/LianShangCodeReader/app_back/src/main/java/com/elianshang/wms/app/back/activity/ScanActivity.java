package com.elianshang.wms.app.back.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.bean.BackList;
import com.elianshang.wms.app.back.provider.ScanProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;

import java.util.ArrayList;

public class ScanActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private LinearLayout inputLayout;

    private TextView titleTextView;

    private Button submitButton;

    /**
     * 动态布局记录列表
     */
    private ArrayList<ViewHolder> vhList = new ArrayList<>();

    /**
     * 最大item数量
     */
    private final int MAX_BOARD_NUM = 100;

    private boolean isItemClick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sacn);

        if (readExtra()) {
            findView();
        }
    }


    private void findView() {

        inputLayout = (LinearLayout) findViewById(R.id.input_Layout);
        titleTextView = (TextView) findViewById(R.id.title_TextView);
        submitButton = (Button) findViewById(R.id.submit_Button);

        submitButton.setOnClickListener(this);
        submitButton.setEnabled(false);

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

        uId = "2";
        uToken = "1231231231231";
        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
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
            new ScanTask(that, getCodeListString()).start();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        addItemView(s);
    }


    private void addItemView(String containerId) {
        if (vhList.size() >= MAX_BOARD_NUM) {
            ToastTool.show(that, "超出单次操作最大品项数");
            return;
        }
        //允许托盘码相同
        for (ViewHolder viewHolder : vhList) {
            if (TextUtils.equals(containerId, viewHolder.codeEditText.getText().toString())) {
                ToastTool.show(that, "该品项已经存在");
                return;
            }
        }

        final View view = View.inflate(that, R.layout.scan_item_view, null);

        ScanEditText containerIdEditText = (ScanEditText) view.findViewById(R.id.containerId_EditText);
        TextView deleteTextView = (TextView) view.findViewById(R.id.delete_TextView);

        inputLayout.addView(view);
        containerIdEditText.setText(containerId);

        final ViewHolder vh = new ViewHolder();
        vh.layout = view;
        vh.codeEditText = containerIdEditText;
        vh.deleteTextView = deleteTextView;
        vhList.add(vh);

        titleTextView.setText("品项数:" + vhList.size());

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
            titleTextView.setText("品项数:" + vhList.size());
        }

        if (vhList.size() < 1) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
        }
    }

    private String getCodeListString() {
        final JSONArray jsonarray = new JSONArray();

        for (ViewHolder vh : vhList) {
            String containerId = vh.codeEditText.getText().toString();
            if (!TextUtils.isEmpty(containerId)) {
                jsonarray.put(containerId);
            }
        }

        return jsonarray.toString();
    }


    private class ViewHolder {

        View layout;

        ScanEditText codeEditText;

        TextView deleteTextView;
    }

    private class ScanTask extends HttpAsyncTask<BackList> {

        private String codeList;

        public ScanTask(Context context, String codeList) {
            super(context, true, true);
            this.codeList = codeList;
        }

        @Override
        public DataHull<BackList> doInBackground() {
            return ScanProvider.request(context, uId, uToken, codeList);
        }

        @Override
        public void onPostExecute(BackList result) {
            BackActivity.launch(ScanActivity.this, uId, uToken, result);
        }
    }
}
