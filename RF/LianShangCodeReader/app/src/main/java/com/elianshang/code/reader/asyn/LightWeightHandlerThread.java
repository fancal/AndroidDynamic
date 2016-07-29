package com.elianshang.code.reader.asyn;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class LightWeightHandlerThread extends HandlerThread {

    private Handler mHandler;

    public LightWeightHandlerThread() {
        super("LightWeight");
        start();
        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                LightWeightHandlerThread.this.handleMessage(msg);
            }
        };
    }

    protected void handleMessage(Message msg) {

    }

    public void sendMessageAtFrontOfQueue(int what, Object object) {
        Message message = mHandler.obtainMessage(what, object);
        mHandler.sendMessageAtFrontOfQueue(message);
    }

    public void removeMessages(int msg) {
        mHandler.removeMessages(msg);
    }

    public void sendMessage(int what, Object object) {
        Message message = mHandler.obtainMessage(what, object);
        mHandler.sendMessage(message);
    }

    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public void sendEmptyMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    public void sendEmptyMessage(int what, int delay) {
        mHandler.sendEmptyMessageDelayed(what, delay);
    }
}
