package com.elianshang.wms.rf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.wms.rf.BaseApplication;
import com.elianshang.wms.rf.R;
import com.elianshang.wms.rf.bean.Menu;
import com.elianshang.wms.rf.bean.MenuList;
import com.elianshang.wms.rf.bean.ResponseState;
import com.elianshang.wms.rf.plugin.PluginStarter;
import com.elianshang.wms.rf.provider.GetMenuListProvider;
import com.elianshang.wms.rf.provider.LogoutProvider;
import com.xue.http.impl.DataHull;

public class MainActivity extends Activity implements View.OnClickListener {

    private MenuList menuList;

    private Button logoutButton;

    private RecyclerView mRecyclerView;

    private PowerManager.WakeLock mWklk;

    private PluginListAdapter adapter;

    private View doubleClickWaitView;

    private boolean clickWait = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                doubleClickWaitView = null;
            } else if(msg.what == 2){
                clickWait = false ;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readCheckLogout(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ScanManager.get() != null){
            ScanManager.get().openSoundControl(this);
        }

        if (!BaseApplication.get().isLogin()) {
            LoginActivity.launch(this);
        }

        findViews();

        ScanManager.get().open();

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        mWklk.acquire(); //设置保持唤醒
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.get().isLogin()) {
            logoutButton.setText("用户:(" + BaseApplication.get().getUser().getUserName() + ")登出");

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

    private void readCheckLogout(Intent intent) {
        if (intent != null) {
            if ("com.elianshang.bridge.logout".equals(intent.getAction())) {
                logout(false);
            }
        }
    }

    private void findViews() {
        logoutButton = (Button) findViewById(R.id.logout_Button);
        mRecyclerView = (RecyclerView) findViewById(R.id.pluginList_RecyclerView);

        logoutButton.setOnClickListener(this);
    }

    private void fillData() {
        if (adapter == null) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(layoutManager);
            adapter = new PluginListAdapter(this);
            mRecyclerView.setAdapter(adapter);
        }

        int spanCount;
        if (menuList == null || menuList.size() <= 4) {
            spanCount = 1;
        } else if (menuList.size() <= 8) {
            spanCount = 2;
        } else if (menuList.size() <= 12) {
            spanCount = 3;
        } else {
            spanCount = 4;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(layoutManager);

        int rows = (int) ((menuList.size() / (float) spanCount) + 0.5f);
        rows = Math.min(rows, 4);//最多4行平分界面

        adapter.setRows(rows);
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

    private void logout(boolean isInitiative) {
        if (isInitiative) {
            new LogoutTask(this, BaseApplication.get().getUserId(), BaseApplication.get().getUserToken()).start();
        } else {
            BaseApplication.get().setUser(null);
            LoginActivity.launch(MainActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWklk.release();
    }

    public void onClick(View v, Menu pluginSource) {
        if (clickWait) {
            return;
        }
        if (doubleClickWaitView == null) {//第一下点击
            doubleClickWaitView = v;

            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, 300);
        } else {
            if (doubleClickWaitView == v) {//第二下点击同一个view
                doubleClickWaitView = null;

                PluginStarter starter = new PluginStarter(this, pluginSource);
                starter.execute();

                handler.removeMessages(2);
                handler.sendEmptyMessageDelayed(2, 500);
            } else {//第二下点击不同的view
                doubleClickWaitView = v;

                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, 300);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == logoutButton) {
            DialogTools.showTwoButtonDialog(this, "确认登出设备吗", "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout(true);
                }
            }, true);
        }
    }

    public static class PluginListAdapter extends RecyclerView.Adapter<PluginViewHolder> {

        private MainActivity context;

        private MenuList list;

        private int rows;

        PluginListAdapter(MainActivity context) {
            this.context = context;
        }

        public void setList(MenuList list) {
            this.list = list;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        @Override
        public PluginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PluginViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plugin_item, null), parent.getHeight());
        }

        @Override
        public void onBindViewHolder(PluginViewHolder holder, int position) {
            holder.fillItem(context, list.get(position), rows);
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

        int parentH;

        public PluginViewHolder(View itemView, int parentH) {
            super(itemView);
            this.parentH = parentH;
            itemTextView = (TextView) itemView.findViewById(R.id.item_TextView);
        }

        public void fillItem(final MainActivity mainActivity, final Menu pluginSource, int rows) {
            itemTextView.getLayoutParams().height = parentH / rows - 2;
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
            super(context, isNew, isNew, true, isNew);
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

    private class LogoutTask extends HttpAsyncTask<ResponseState> {

        private String uId;

        private String uToken;

        public LogoutTask(Context context, String uId, String uToken) {
            super(context, true, true, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return LogoutProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            if (!MainActivity.this.isDestroyed()) {
                BaseApplication.get().setUser(null);
                LoginActivity.launch(MainActivity.this);
            }
        }
    }
}
