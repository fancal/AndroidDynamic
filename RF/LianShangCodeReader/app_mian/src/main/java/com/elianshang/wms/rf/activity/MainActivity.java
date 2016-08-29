package com.elianshang.wms.rf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.wms.rf.BaseApplication;
import com.elianshang.wms.rf.R;
import com.elianshang.wms.rf.bean.MenuList;
import com.elianshang.wms.rf.bean.Menu;
import com.elianshang.wms.rf.plugin.PluginStarter;
import com.elianshang.wms.rf.provider.GetMenuListProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends Activity {

    private MenuList menuList;

    private RecyclerView mRecyclerView;

    private PowerManager.WakeLock mWklk;

    private PluginListAdapter adapter;

    private View doubleClickWaitView;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                doubleClickWaitView = null;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!BaseApplication.get().isLogin()) {
            LoginActivity.launch(this);
        }

        findViews();

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        mWklk.acquire(); //设置保持唤醒
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.get().isLogin()) {
            boolean isNew = false;
            if (menuList == null) {
                isNew = true;
            }
            new RequestMenuListTask(this, BaseApplication.get().getUserId(), BaseApplication.get().getUserToken(), isNew).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.pluginList_RecyclerView);
    }

    private void fillData() {
        if (adapter == null) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(layoutManager);
            adapter = new PluginListAdapter(this);
            mRecyclerView.setAdapter(adapter);
        }

        if (menuList == null || menuList.size() <= 3) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            mRecyclerView.setLayoutManager(layoutManager);
        } else if (menuList.size() <= 6) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        adapter.setList(menuList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode != RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWklk.release();
    }

    public void onClick(View v, Menu pluginSource) {
        if (doubleClickWaitView == null) {//第一下点击
            doubleClickWaitView = v;

            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, 200);
        } else {
            if (doubleClickWaitView == v) {//第二下点击同一个view
                doubleClickWaitView = null;

                PluginStarter starter = new PluginStarter(this, pluginSource);
                starter.execute();
            } else {//第二下点击不同的view
                doubleClickWaitView = v;

                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, 200);
            }
        }
    }

    public static class PluginListAdapter extends RecyclerView.Adapter<PluginViewHolder> {

        private MainActivity context;

        private MenuList list;

        PluginListAdapter(MainActivity context) {
            this.context = context;
        }

        public void setList(MenuList list) {
            this.list = list;
        }

        @Override
        public PluginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PluginViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plugin_item, null));
        }

        @Override
        public void onBindViewHolder(PluginViewHolder holder, int position) {
            holder.fillItem(context, list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            }

            return list.size();
        }
    }

    public static class PluginViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;

        public PluginViewHolder(View itemView) {
            super(itemView);

            itemTextView = (TextView) itemView.findViewById(R.id.item_TextView);
        }

        public void fillItem(final MainActivity mainActivity, final Menu pluginSource) {
            itemTextView.setText(pluginSource.getName());
            itemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onClick(v, pluginSource);
                }
            });
        }
    }

    private class RequestMenuListTask extends HttpAsyncTask<MenuList> {

        private String uId;

        private String uToken;

        public RequestMenuListTask(Context context, String uId, String uToken, boolean isNew) {
            super(context, isNew, isNew, true);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<MenuList> doInBackground() {
            DataHull<MenuList> dataHull = GetMenuListProvider.request(context, uId, uToken);
            return dataHull;
        }

        @Override
        public void onPostExecute(MenuList result) {
            menuList = result;
            fillData();
        }
    }
}
