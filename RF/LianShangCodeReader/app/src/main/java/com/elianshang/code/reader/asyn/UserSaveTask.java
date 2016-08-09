package com.elianshang.code.reader.asyn;


import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.db.PreferencesManager;

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
