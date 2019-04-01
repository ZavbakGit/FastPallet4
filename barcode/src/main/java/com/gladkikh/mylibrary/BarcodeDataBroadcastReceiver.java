package com.gladkikh.mylibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Alex on 24.05.2017.
 */

public class BarcodeDataBroadcastReceiver extends BroadcastReceiver {

    ReciveBarcode mResiveBarcode;

    public BarcodeDataBroadcastReceiver(ReciveBarcode resiveBarcode) {
        mResiveBarcode = resiveBarcode;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String barcode = intent.getStringExtra("com.hht.emdk.datawedge.data_string");
        mResiveBarcode.callBack(barcode);
    }

    public interface ReciveBarcode {
        void callBack(String barcode);
    }


}
