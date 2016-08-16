package com.elianshang.code.pick.asyn;


import com.elianshang.bridge.asyn.SimpleAsyncTask;
import com.elianshang.code.pick.bean.User;
import com.elianshang.code.pick.db.PreferencesManager;

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
