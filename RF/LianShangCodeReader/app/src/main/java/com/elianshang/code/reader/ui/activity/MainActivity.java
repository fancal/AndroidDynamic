package com.elianshang.code.reader.ui.activity;

import android.barcode.BarCodeManager;
import android.os.Bundle;
import android.util.Log;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    private BarCodeManager mBarCode;
    private BarCodeManager.OnBarCodeReceivedListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBarCode = (BarCodeManager) getSystemService("barcode");
        openBCR();
    }

    public void openBCR()
    {
//        try {
//            if(mBarCode.open()) {
//                BarCodeManager.BCR_TYPE bcr_type = mBarCode.getBarCodeReaderType();
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }

        mListener = new BarCodeManager.OnBarCodeReceivedListener() {

            @Override
            public void OnBarCodeReceived(String arg0) {
                Log.e("xue" , "arg0 == " + arg0);
//                try {
//
//                    if(mBarCode.getAPIVersion() >= 0x14) {
//
//                        // Prefer: fastest method (KBE Method 1:) for general case
//                        mBarCode.setBarCodeToFocusView(arg0);
//
////						Test: KBE Method 3: Send key events used in special purpose (eg. Remote desktop application)
////						mBarCode.sendString(arg0+"\n");// Added in API v1.04 and higher
//                    }
//                    else {
//                        // Prefer: fastest method (KBE Method 1:) for general case
//                        mBarCode.setBarCodeToFocusView(arg0);
//                    }
//                }
//                catch (Exception e) {
//                }
            }
        };

        mBarCode.addListener(mListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBCR();
    }

    public void closeBCR()
    {
        mBarCode.removeListener(mListener);
        mBarCode.close();
    }
}
