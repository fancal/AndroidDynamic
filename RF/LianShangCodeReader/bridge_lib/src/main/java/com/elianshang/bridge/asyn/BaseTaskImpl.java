package com.elianshang.bridge.asyn;


import android.os.AsyncTask;
import android.os.Looper;

import com.elianshang.tools.WeakReferenceHandler;

public abstract class BaseTaskImpl<D, R> implements AsyncTaskInterface<D, R> {

    private WeakReferenceHandler handler;

    AsyncTask<Void, Void, R> asyncTask = null;

    protected BaseTaskImpl() {
        handler = new WeakReferenceHandler(Looper.getMainLooper());
    }

    public void start() {
        postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<BaseTaskImpl<D, R>>() {
            @Override
            public void run(BaseTaskImpl<D, R> hook) {
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
                asyncTask = new SiT();
                asyncTask.execute();
            }
        });
    }

    public void cancel() {
        asyncTask.cancel(true);
    }

    public boolean isCancel() {
        return asyncTask.isCancelled();
    }

    public abstract R run();

    protected final <Hook> void postUI(Hook hook, WeakReferenceHandler.WeakReferenceHandlerRunnalbe<Hook> runnable) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(hook, runnable);
        } else {
            runnable.run(hook);
        }
    }

    private class SiT extends AsyncTask<Void, Void, R> {

        @Override
        protected void onPreExecute() {
            BaseTaskImpl.this.onPreExecute();
        }

        @Override
        protected R doInBackground(Void... params) {
            return run();
        }

        @Override
        protected void onPostExecute(R r) {
            if (r != null) {
                BaseTaskImpl.this.onPostExecute(r);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(R r) {
            super.onCancelled(r);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
