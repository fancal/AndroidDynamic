package com.elianshang.wms.app.takestock.ui.activity;

import android.os.Bundle;

import com.ryg.dynamicload.DLBasePluginActivity;

public class MainActivity extends DLBasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TakeStockActivity.launch(that, "");
    }
}
