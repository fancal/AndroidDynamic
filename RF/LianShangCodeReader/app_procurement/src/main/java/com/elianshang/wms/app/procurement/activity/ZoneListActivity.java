package com.elianshang.wms.app.procurement.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.bean.ResponseState;
import com.elianshang.wms.app.procurement.bean.ZoneList;
import com.elianshang.wms.app.procurement.provider.ZoneListProvider;
import com.elianshang.wms.app.procurement.provider.ZoneLoginProvider;
import com.xue.http.impl.DataHull;

public class ZoneListActivity extends DLBasePluginActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbar;

    private String uId;

    private String uToken;

    private ZoneList zoneList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView typeNameTextView;

    private ListView zoneListView;

    private ZoneListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zonelist_activity);
        if (readExtras()) {
            findView();
            initToolbar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new ZoneListTask().start();
    }

    private void findView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        typeNameTextView = (TextView) findViewById(R.id.transfer_type_name);
        zoneListView = (ListView) findViewById(R.id.zone_ListView);
        zoneListView.setOnItemClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
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

    private void fillData() {
        if (zoneList == null) {
            typeNameTextView.setText("没有补货任务,下拉刷新");
            zoneListView.setVisibility(View.GONE);
            return;
        }

        zoneListView.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new ZoneListAdapter();
            zoneListView.setAdapter(adapter);
        }

        typeNameTextView.setText("补货区域选择");
        adapter.notifyDataSetChanged();
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "1";
//        uToken = "198302935052918";
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (zoneList != null && position < zoneList.size()) {
            new ZoneLoginTask(zoneList.get(position).getZoneId()).start();
        }
    }

    @Override
    public void onRefresh() {
        new ZoneListTask().start();
    }

    private class ZoneListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (zoneList == null) {
                return 0;
            }
            return zoneList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;
            ZoneList.Zone zone = zoneList.get(position);
            if (convertView == null) {
                convertView = View.inflate(that, R.layout.zone_item, null);

                viewHodler = new ViewHodler();

                viewHodler.zoneNameTextView = (TextView) convertView.findViewById(R.id.zoneName_TextView);
                viewHodler.taskNumTextView = (TextView) convertView.findViewById(R.id.taskNum_TextView);
                viewHodler.workerNumTextView = (TextView) convertView.findViewById(R.id.workerNum_TextView);

                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            viewHodler.zoneNameTextView.setText(zone.getZoneName());
            viewHodler.taskNumTextView.setText("任务：" + zone.getTaskNum());
            viewHodler.workerNumTextView.setText("人员：" + zone.getWorkerNum());

            return convertView;
        }

        private class ViewHodler {
            TextView zoneNameTextView;
            TextView taskNumTextView;
            TextView workerNumTextView;
        }
    }

    private class ZoneListTask extends HttpAsyncTask<ZoneList> {

        public ZoneListTask() {
            super(ZoneListActivity.this.that, true, true, false, false);
        }

        @Override
        public DataHull<ZoneList> doInBackground() {
            return ZoneListProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(ZoneList result) {
            swipeRefreshLayout.setRefreshing(false);
            zoneList = result;
            fillData();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            swipeRefreshLayout.setRefreshing(false);
            zoneList = null;
            fillData();
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void netNull() {
            super.netNull();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class ZoneLoginTask extends HttpAsyncTask<ResponseState> {

        private String zoneId;

        public ZoneLoginTask(String zoneId) {
            super(ZoneListActivity.this.that, true, true, false, false);
            this.zoneId = zoneId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ZoneLoginProvider.request(context, uId, uToken, zoneId);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            MainActivity.launch(ZoneListActivity.this, uId, uToken, zoneId);
        }
    }
}
