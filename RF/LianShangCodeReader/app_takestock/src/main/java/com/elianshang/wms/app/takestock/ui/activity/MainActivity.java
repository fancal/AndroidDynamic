package com.elianshang.wms.app.takestock.ui.activity;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TakeStockActivity.launch(this, "");
    }
}
