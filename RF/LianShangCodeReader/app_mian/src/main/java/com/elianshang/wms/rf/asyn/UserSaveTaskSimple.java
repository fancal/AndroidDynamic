package com.elianshang.wms.rf.asyn;


import com.elianshang.bridge.asyn.SimpleAsyncTask;
import com.elianshang.bridge.tool.HostTool;
import com.elianshang.wms.rf.PreferencesManager;
import com.elianshang.wms.rf.bean.User;

public class UserSaveTaskSimple extends SimpleAsyncTask<Void> {

    private User user;

    public UserSaveTaskSimple(User user) {
        this.user = user;
    }

    @Override
    public Void doInBackground() {
        PreferencesManager.get().setUser(user);
        PreferencesManager.get().setHost(HostTool.curHost.getHostUrl());
        return null;
    }

    @Override
    public void onPostExecute(Void result) {

    }

    @Override
    public void onCancelled() {

    }
}
