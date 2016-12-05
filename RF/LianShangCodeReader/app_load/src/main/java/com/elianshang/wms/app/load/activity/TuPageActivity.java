package com.elianshang.wms.app.load.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.adapter.TuListAdapter;
import com.elianshang.wms.app.load.bean.TuJobList;
import com.elianshang.wms.app.load.bean.TuList;
import com.elianshang.wms.app.load.provider.TuJobListProvier;
import com.elianshang.wms.app.load.provider.TuListProvier;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuPageActivity extends DLBasePluginActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View tabLayout;

    private Button tab1;

    private Button tab2;

    private View unloadLayout;

    private ListView unloadListView;

    private View unloadEmptyView;

    private View loadedLayout;

    private ListView loadedListView;

    private View loadedEmptyView;

    private View detailLayout;

    private TextView detailTuTextView;

    private TextView detailCarNumberTextView;

    private TextView detailDriverNameTextView;

    private TextView detailPreBoardTextView;

    private TextView detailStoresTextView;

    private Button detailNextButton;

    private String[] statusArray = new String[]{"1", "5"};

    private TuListAdapter unloadListAdapter;

    private TuListAdapter loadedListAdapter;

    private TuList unloadTuList;

    private TuList loadedTuList;

    private TuList.Item curItem;

    private TuListTask tuListTask;

    private TuJobListTask tuJobListTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity_tupage);
        if (readExtras()) {
            findView();
        }
    }

    private boolean readExtras() {
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

    private void findView() {
        initToolbar();

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (Button) tabLayout.findViewById(R.id.tab1);
        tab2 = (Button) tabLayout.findViewById(R.id.tab2);

        unloadLayout = findViewById(R.id.unloadLayout);
        unloadListView = (ListView) unloadLayout.findViewById(R.id.listview);
        unloadEmptyView = unloadLayout.findViewById(R.id.empty_TextView);

        loadedLayout = findViewById(R.id.loadedLayout);
        loadedListView = (ListView) loadedLayout.findViewById(R.id.listview);
        loadedEmptyView = loadedLayout.findViewById(R.id.empty_TextView);

        detailLayout = findViewById(R.id.detailLayout);
        detailTuTextView = (TextView) detailLayout.findViewById(R.id.tu_TextView);
        detailCarNumberTextView = (TextView) detailLayout.findViewById(R.id.carNumber_TextView);
        detailDriverNameTextView = (TextView) detailLayout.findViewById(R.id.driverName_TextView);
        detailPreBoardTextView = (TextView) detailLayout.findViewById(R.id.preBoard_TextView);
        detailStoresTextView = (TextView) detailLayout.findViewById(R.id.stores_TextView);
        detailNextButton = (Button) detailLayout.findViewById(R.id.next_Button);

        detailNextButton.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);

        tab1.performClick();
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
    public void onBackPressed() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            if (tab1.isSelected()) {
                requestUnloadList();
            } else {
                requestLoadedList();
            }
            return;
        }
        finish();
    }

    private void requestUnloadList() {
        tabLayout.setVisibility(View.VISIBLE);
        unloadLayout.setVisibility(View.VISIBLE);
        loadedLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);

        if (tuListTask != null) {
            tuListTask.cancel();
            tuListTask = null;
        }
        tuListTask = new TuListTask(that, statusArray[0]);
        tuListTask.start();

    }

    private void requestLoadedList() {
        tabLayout.setVisibility(View.VISIBLE);
        unloadLayout.setVisibility(View.GONE);
        loadedLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        if (tuListTask != null) {
            tuListTask.cancel();
            tuListTask = null;
        }
        tuListTask = new TuListTask(that, statusArray[1]);
        tuListTask.start();
    }

    private void fillUnloadList() {
        if (unloadTuList != null) {
            unloadEmptyView.setVisibility(View.GONE);
            unloadListView.setVisibility(View.VISIBLE);
            if (unloadListAdapter == null) {
                unloadListAdapter = new TuListAdapter(that);
                unloadListView.setAdapter(unloadListAdapter);
            }
            unloadListAdapter.setTuList(unloadTuList);
            unloadListAdapter.notifyDataSetChanged();
            unloadListView.setOnItemClickListener(this);
        } else {
            unloadEmptyView.setVisibility(View.VISIBLE);
            unloadListView.setVisibility(View.GONE);
        }
    }

    private void fillLoadedList() {
        if (loadedTuList != null) {
            loadedEmptyView.setVisibility(View.GONE);
            loadedListView.setVisibility(View.VISIBLE);
            if (loadedListAdapter == null) {
                loadedListAdapter = new TuListAdapter(that);
                loadedListView.setAdapter(loadedListAdapter);
            }
            loadedListAdapter.setTuList(loadedTuList);
            loadedListAdapter.notifyDataSetChanged();
            loadedListView.setOnItemClickListener(this);
        } else {
            loadedEmptyView.setVisibility(View.VISIBLE);
            loadedListView.setVisibility(View.GONE);
        }
    }

    private void fillTuDetail(TuList.Item item) {
        tabLayout.setVisibility(View.GONE);
        unloadLayout.setVisibility(View.GONE);
        loadedLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        this.curItem = item;
        detailTuTextView.setText("TU号:" + item.getTu());
        detailCarNumberTextView.setText("车牌号:" + item.getCarNumber());
        detailDriverNameTextView.setText("司机电话:" + item.getCellphone());
        detailPreBoardTextView.setText("预装版数:" + item.getPreBoard());
        if (item.getStores() != null && item.getStores().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < item.getStores().size(); i++) {
                TuList.Item.Store store = item.getStores().get(i);
                sb.append("门店");
                sb.append(String.valueOf(i + 1));
                sb.append(":");
                sb.append(store.getCustomerName());
                sb.append("【");
                sb.append(store.getCustomerCode());
                sb.append("】");
                if (i != item.getStores().size() - 1) {
                    sb.append("\r\n");
                }
            }
            detailStoresTextView.setText(sb.toString());
        }

        detailNextButton.setText(TextUtils.equals(statusArray[0], item.getStatus()) ? "开始装车" : "追加装车");
    }

    @Override
    public void onClick(View v) {
        if (v == tab1) {
            tab1.setSelected(true);
            tab2.setSelected(false);
            requestUnloadList();
        } else if (v == tab2) {
            tab1.setSelected(false);
            tab2.setSelected(true);
            requestLoadedList();
        } else if (v == detailNextButton) {
            if (tuJobListTask != null) {
                tuJobListTask.cancel();
                tuJobListTask = null;
            }
            tuJobListTask = new TuJobListTask(that, curItem.getTu());
            tuJobListTask.start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (tab1.isSelected()) {
            fillTuDetail(unloadTuList.get(position));
        } else if (tab2.isSelected()) {
            fillTuDetail(loadedTuList.get(position));
        }
    }

    /**
     * 请求尾货接口
     */
    private class TuJobListTask extends HttpAsyncTask<TuJobList> {

        private String tu;

        public TuJobListTask(Context context, String tu) {
            super(context, true, true, false);
            this.tu = tu;
        }

        @Override
        public DataHull<TuJobList> doInBackground() {
            return TuJobListProvier.request(context, uId, uToken, tu);
        }

        @Override
        public void onPostExecute(TuJobList result) {
            TuJobActivity.launch(TuPageActivity.this, uId, uToken, tu, result);
        }

        @Override
        public void dataNull(String errMsg) {
            TuJobActivity.launch(TuPageActivity.this, uId, uToken, tu, null);
        }
    }

    /**
     * 获取 未装车/已装车列表
     */
    private class TuListTask extends HttpAsyncTask<TuList> {

        private String status;

        public TuListTask(Context context, String status) {
            super(context, true, true, false);
            this.status = status;
        }

        @Override
        public DataHull<TuList> doInBackground() {
            return TuListProvier.request(context, uId, uToken, status);
        }

        @Override
        public void onPostExecute(TuList result) {

            if (TextUtils.equals(TuPageActivity.this.statusArray[0], status)) {
                unloadTuList = result;
                for (TuList.Item item : unloadTuList) {//手动设置每个item的status
                    item.setStatus(status);
                }
                fillUnloadList();
            } else {
                loadedTuList = result;
                for (TuList.Item item : loadedTuList) {//手动设置每个item的status
                    item.setStatus(status);
                }
                fillLoadedList();
            }
        }

        @Override
        public void dataNull(String errMsg) {
            if (TextUtils.equals(TuPageActivity.this.statusArray[0], status)) {
                unloadTuList = null;
                fillUnloadList();
            } else {
                loadedTuList = null;
                fillLoadedList();
            }
        }
    }

}
