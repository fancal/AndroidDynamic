package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.asyn.LocationProductListTask;
import com.elianshang.code.reader.bean.Product;
import com.elianshang.code.reader.bean.ProductList;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class TransferLocationActivity extends BaseActivity implements ScanEditTextTool.OnSetComplete, ScanManager.OnBarCodeListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, TransferLocationActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private ScanEditText mLocationIdEditText;
    private AppCompatSpinner mItemSpinner;
    private AppCompatSpinner mPackSpinner;
    private AppCompatEditText mQtyEditText;
    private ScanEditTextTool scanEditTextTool;
    private Button button;
    private ArrayAdapter<String> mItemAdapter;
    private ArrayAdapter<String> mPackAdapter;
    private ProductList mProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scrap);

        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }


    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLocationIdEditText = (ScanEditText) findViewById(R.id.scrap_locationid);
        mItemSpinner = (AppCompatSpinner) findViewById(R.id.scrap_itemid);
        mPackSpinner = (AppCompatSpinner) findViewById(R.id.scrap_pack_name);
        mQtyEditText = (AppCompatEditText) findViewById(R.id.scrap_qty);
        button = (Button) findViewById(R.id.button);

        scanEditTextTool = new ScanEditTextTool(this, mLocationIdEditText);
        scanEditTextTool.setComplete(this);

        button.setEnabled(false);
        button.setClickable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestCreateScrapTask(TransferLocationActivity.this, mProducts.get(mItemSpinner.getSelectedItemPosition()).getItemId(), mLocationIdEditText.getText().toString(), mPackSpinner.getSelectedItem().toString(), mQtyEditText.getText().toString()).start();

            }
        });
        mItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                check();
                mPackAdapter = new ArrayAdapter<>(TransferLocationActivity.this, android.R.layout.simple_spinner_dropdown_item, mProducts.get(position).getPackName());
                mPackSpinner.setAdapter(mPackAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                check();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mQtyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void check() {
        boolean check = !TextUtils.isEmpty(mLocationIdEditText.getText().toString());
        check &= !TextUtils.isEmpty(mItemSpinner.getSelectedItem().toString());
        check &= !TextUtils.isEmpty(mPackSpinner.getSelectedItem().toString());
        check &= !TextUtils.isEmpty(mQtyEditText.getText().toString());

        button.setEnabled(check);
        button.setClickable(check);

    }

    private void fill() {
        ArrayList<String> items = new ArrayList<>();
        for (Product product : mProducts) {
            items.add(product.getName());
        }
        mItemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mItemSpinner.setAdapter(mItemAdapter);
        mPackAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mProducts.get(0).getPackName());
        mPackSpinner.setAdapter(mPackAdapter);
    }

    @Override
    public void onSetComplete() {
        button.setEnabled(false);
        button.setClickable(false);
        String locationId = mLocationIdEditText.getText().toString();
        locationId = "19";
        new LocationProductListTask(this, locationId, new LocationProductListTask.CallBack() {
            @Override
            public void success(ProductList products) {
                if (mProducts == null) {
                    mProducts = new ProductList();
                }
                mProducts.clear();
                mProducts.addAll(products);
                fill();
            }

            @Override
            public void failed(String errStr) {

            }
        }).start();
    }

    @Override
    public void onInputError(int i) {
        button.setEnabled(false);
        button.setClickable(false);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    private class RequestCreateScrapTask extends HttpAsyncTask<ResponseState> {

        private String itemId;
        private String locationId;
        private String packName;
        private String qty;

        public RequestCreateScrapTask(Context context, String itemId, String locationId, String packName, String qty) {
            super(context, true, true, false);
            this.itemId = itemId;
            this.locationId = locationId;
            this.qty = qty;
            this.packName = packName;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.inhouseCreateScrap(itemId, locationId, packName, qty, BaseApplication.get().getUser().getUid());
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            ToastTool.show(context, "success");
        }
    }


}
