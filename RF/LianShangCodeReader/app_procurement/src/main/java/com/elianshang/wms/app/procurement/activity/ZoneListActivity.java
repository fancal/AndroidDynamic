package com.elianshang.wms.app.procurement.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.bean.LocationList;
import com.elianshang.wms.app.procurement.bean.ResponseState;
import com.elianshang.wms.app.procurement.bean.ZoneList;
import com.elianshang.wms.app.procurement.provider.LocationListProvider;
import com.elianshang.wms.app.procurement.provider.ZoneListProvider;
import com.elianshang.wms.app.procurement.provider.ZoneLoginProvider;
import com.elianshang.wms.app.procurement.provider.ZoneLogoutProvider;
import com.xue.http.impl.DataHull;

public class ZoneListActivity extends DLBasePluginActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private Toolbar mToolbar;

    private String uId;

    private String uToken;

    private ZoneList zoneList;

    private LocationList locationList;

    private TextView typeNameTextView;

    private SwipeRefreshLayout zoneSwipeRefreshLayout;

    private ListView zoneListView;

    private SwipeRefreshLayout locationSwipeRefreshLayout;

    private ListView locationListView;

    private ZoneListAdapter zoneAdapter;

    private LocationListAdapter locationAdapter;

    private Button systemFetchButton;

    private boolean isItemClick = false;

    private String zoneId;

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

        if (zoneSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            new ZoneListTask().start();
        } else if (locationSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            new LocationListTask(zoneId).start();
        }
    }

    private void findView() {
        systemFetchButton = (Button) findViewById(R.id.systemFetch_Button);
        zoneSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.zone_SwipeRefreshLayout);
        locationSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.location_SwipeRefreshLayout);

        typeNameTextView = (TextView) findViewById(R.id.transfer_type_name);
        zoneListView = (ListView) findViewById(R.id.zone_ListView);
        locationListView = (ListView) findViewById(R.id.location_ListView);
        zoneListView.setOnItemClickListener(this);
        locationListView.setOnItemClickListener(this);

        systemFetchButton.setOnClickListener(this);
        zoneSwipeRefreshLayout.setOnRefreshListener(this);
        locationSwipeRefreshLayout.setOnRefreshListener(this);
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

    @Override
    public void onBackPressed() {
        if (zoneSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            finish();
        } else if (locationSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(that, "退出该区域补货", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ZoneLogoutTask(zoneId).start();
                }
            }, true);
        } else {
            finish();
        }
    }

    private void fillZoneListData() {
        systemFetchButton.setVisibility(View.GONE);
        zoneSwipeRefreshLayout.setVisibility(View.VISIBLE);
        locationSwipeRefreshLayout.setVisibility(View.GONE);

        if (zoneList == null) {
            typeNameTextView.setText("没有补货任务,下拉刷新");
            zoneAdapter = null;
            zoneListView.setAdapter(null);
            zoneListView.setVisibility(View.GONE);
            return;
        }

        if (zoneAdapter == null) {
            zoneAdapter = new ZoneListAdapter();
            zoneListView.setAdapter(zoneAdapter);
        }

        typeNameTextView.setText("补货区域选择");
        zoneAdapter.notifyDataSetChanged();
    }

    private void fillLocationListData() {
        systemFetchButton.setVisibility(View.VISIBLE);
        zoneSwipeRefreshLayout.setVisibility(View.GONE);
        locationSwipeRefreshLayout.setVisibility(View.VISIBLE);

        if (locationList == null) {
            typeNameTextView.setText("没有补货任务,下拉刷新");
            locationAdapter = null;
            locationListView.setAdapter(null);
            locationListView.setVisibility(View.GONE);
            return;
        }

        if (locationAdapter == null) {
            locationAdapter = new LocationListAdapter();
            locationListView.setAdapter(locationAdapter);
        }

        typeNameTextView.setText("补货任务选择");
        locationAdapter.notifyDataSetChanged();
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "1";
//        uToken = "198302935052918";
//        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        if (parent == zoneListView) {
            if (zoneList != null && position < zoneList.size()) {
                new ZoneLoginTask(zoneList.get(position).getZoneId()).start();
            }
        } else if (parent == locationListView) {
            if (locationList != null && position < locationList.size()) {
                MainActivity.launch(this, uId, uToken, null, locationList.get(position).getTaskId());
            }
        }
    }

    @Override
    public void onRefresh() {
        if (zoneSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            new ZoneListTask().start();
        } else if (locationSwipeRefreshLayout.getVisibility() == View.VISIBLE) {
            new LocationListTask(zoneId).start();
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

        if (v == systemFetchButton) {
            MainActivity.launch(this, uId, uToken, zoneId, null);
        }
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

    private class LocationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (locationList == null) {
                return 0;
            }
            return locationList.size();
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
            LocationList.Location location = locationList.get(position);
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

            viewHodler.zoneNameTextView.setText(location.getLocationCode());
            viewHodler.taskNumTextView.setText("优先级：" + location.getPriority());
            viewHodler.workerNumTextView.setText("箱数：" + location.getPackCount());

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
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
            zoneList = result;
            fillZoneListData();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
            zoneList = null;
            fillZoneListData();
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void netNull() {
            super.netNull();
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class LocationListTask extends HttpAsyncTask<LocationList> {

        private String zoneId;

        public LocationListTask(String zoneId) {
            super(ZoneListActivity.this.that, true, true, false, false);
            this.zoneId = zoneId;
        }

        @Override
        public DataHull<LocationList> doInBackground() {
            return LocationListProvider.request(context, uId, uToken, zoneId);
        }

        @Override
        public void onPostExecute(LocationList result) {
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
            locationList = result;
            fillLocationListData();
        }

        @Override
        public void dataNull(String errMsg) {
            ToastTool.show(context , "该区域没有补货任务");
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
            locationList = null;
            fillZoneListData();
        }

        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void netNull() {
            super.netNull();
            zoneSwipeRefreshLayout.setRefreshing(false);
            locationSwipeRefreshLayout.setRefreshing(false);
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
            ZoneListActivity.this.zoneId = zoneId;
            new LocationListTask(zoneId).start();
        }
    }

    private class ZoneLogoutTask extends HttpAsyncTask<ResponseState> {

        private String zoneId;

        public ZoneLogoutTask(String zoneId) {
            super(ZoneListActivity.this.that, false, false, false, false);
            this.zoneId = zoneId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ZoneLogoutProvider.request(context, uId, uToken, zoneId);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            fillZoneListData();
            new ZoneListTask().start();
        }
    }
}
