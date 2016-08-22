package com.elianshang.wms.rf.asyn;


import com.elianshang.bridge.asyn.SimpleAsyncTask;
import com.elianshang.wms.rf.PreferencesManager;
import com.elianshang.wms.rf.bean.User;

public class UserSaveTask extends SimpleAsyncTask<Void> {

    private User user;

    public UserSaveTask(User user) {
        this.user = user;
    }

    @Override
    public Void doInBackground() {
        PreferencesManager.get().setUser(user);
        return null;
    }

    @Override
    public void onPostExecute(Void result) {

    }
}
