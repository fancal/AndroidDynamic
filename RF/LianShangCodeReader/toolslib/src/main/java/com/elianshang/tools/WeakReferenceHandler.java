package com.elianshang.tools;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 虚引用持有对象的handler，降低代码不当的内存泄露问题
 * <p/>
 * HandleMessage时请不要直接引用其他对象，而是通过Hook操作
 */
public class WeakReferenceHandler extends Handler {

    public WeakReferenceHandler(Looper looper) {
        super(looper);
    }

    @Override
    public final void handleMessage(Message msg) {
    }

    public <Hook> void post(Hook hook, final WeakReferenceHandlerRunnalbe<Hook> runnalbe) {

        final WeakReference<Hook> weakReference = new WeakReference(hook);
        post(new Runnable() {
            @Override
            public void run() {
                Hook hook = weakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        });
    }

    public <Hook> void postAtFrontOfQueue(Hook hook, final WeakReferenceHandlerRunnalbe<? super Hook> runnalbe) {
        final WeakReference<Hook> weakReference = new WeakReference(hook);

        postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                Hook hook = weakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        });
    }

    public <Hook> void postDelayed(Hook hook, final WeakReferenceHandlerRunnalbe<? super Hook> runnalbe, long delayMillis) {
        final WeakReference<Hook> weakReference = new WeakReference(hook);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                Hook hook = weakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        }, delayMillis);
    }

    public <Hook> void postAtTime(Hook hook, final WeakReferenceHandlerRunnalbe<? super Hook> runnalbe, long uptimeMillis) {
        final WeakReference<Hook> weakReference = new WeakReference(hook);

        postAtTime(new Runnable() {
            @Override
            public void run() {
                Hook hook = weakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        }, uptimeMillis);
    }

    public interface WeakReferenceHandlerRunnalbe<H> {
        void run(H hook);
    }
}
