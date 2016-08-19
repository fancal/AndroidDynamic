package com.elianshang.wms.app.transfer.ui.activity;

import android.os.Bundle;

import com.ryg.dynamicload.DLBasePluginActivity;

public class MainActivity extends DLBasePluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uId = getIntent().getStringExtra("uId");
        String uToken = getIntent().getStringExtra("uToken");

    }



}
