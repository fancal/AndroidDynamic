package com.elianshang.wms.rf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.wms.rf.R;

public class WelcomeActivity extends Activity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ScanManager.get().open();
                isOpen = true;
                sendEmptyMessage(3);
            } else if (msg.what == 2) {
                isTimeOut = true;
                sendEmptyMessage(3);
            } else if (msg.what == 3) {
                if (isTimeOut && isOpen) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

    private boolean isTimeOut = false;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler.sendEmptyMessageDelayed(1, 200);
        handler.sendEmptyMessageDelayed(2, 1000);
    }
}
