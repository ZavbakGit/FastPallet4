package com.gladkikh.mylibrary

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject

class BarcodeHelper(private val context: AppCompatActivity,typE_TSD: TYPE_TSD?) : LifecycleObserver {

    private val intent: Intent
    private val mServiceConn: ServiceConnection

    enum class TYPE_TSD(val id:Int,val fullName:String,val action:String,val keyBarcode:String){
        ATOL_SMART_DROID(1,"АТОЛ Smart.Droid",
            "com.motorolasolutions.emdk.sample.dwdemosample.RECVR",
            "com.hht.emdk.datawedge.data_string"),

        ATOL_SMART_LIGHT(2,"АТОЛ Smart.Lite",
            "com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST",
            "EXTRA_BARCODE_DECODING_DATA");

        companion object {
            fun getTypeTSD(id:Int?):TYPE_TSD?{
               return when(id){
                    1 -> ATOL_SMART_DROID
                    2-> ATOL_SMART_LIGHT
                    else -> null
                }
            }
        }

    }

    /**
     * get PublishSubject with barcode
     */
    private val barcodePublishSubject = PublishSubject.create<String?>()


    init {
        intent = Intent(context, BrodcastService::class.java)
        intent.putExtra(BrodcastService.INTENT_KEY_ACTION,typE_TSD?.action)
        intent.putExtra(BrodcastService.INTENT_KEY_KEY_BARCODE,typE_TSD?.keyBarcode)


        mServiceConn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?): Unit {

            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder?): Unit {
                (binder as BrodcastService.MyBinder)
                    .servises.dataPublishSubject
                    .subscribe {
                        barcodePublishSubject.onNext(it!!)
                    }
            }
        }
        context.lifecycle.addObserver(this)
    }

    fun getDataFlowable() = barcodePublishSubject.toFlowable(BackpressureStrategy.BUFFER)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {

        try {
            context.unbindService(mServiceConn)
            context.stopService(intent)
        } catch (e: Exception) {
            System.out.println("ON STOP Unbinding didn't work. little surprise");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onResume() {
        try {
            context.unbindService(mServiceConn)
            context.stopService(intent)
        } catch (e: Exception) {
            System.out.println("ON START Unbinding didn't work. little surprise");
        }


        context.startService(intent)
        context.bindService(
            intent,
            mServiceConn,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }


}