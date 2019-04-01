package com.gladkikh.mylibrary

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import io.reactivex.subjects.PublishSubject


class BrodcastService : Service() {

    val binder = MyBinder()

    val dataPublishSubject = PublishSubject.create<String?>()

    private val mBarcodeDataBroadcastReceiver = BarcodeDataBroadcastReceiver { barcode ->
        dataPublishSubject.onNext(barcode)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter("DATA_SCAN")
        registerReceiver(mBarcodeDataBroadcastReceiver, intentFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Do not forget to unregister the receiver!!!
        this.unregisterReceiver(mBarcodeDataBroadcastReceiver)
    }



    inner class MyBinder : Binder() {
        val servises = this@BrodcastService
    }
}
