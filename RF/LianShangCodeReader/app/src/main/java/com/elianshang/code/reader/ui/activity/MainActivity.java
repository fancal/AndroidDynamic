package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String utoken = BaseApplication.get().getUserToken();
    }
}
