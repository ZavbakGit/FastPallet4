package com.anit.fastpallet4.presentaition.ui.util
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator;

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.PlaySound
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.math.BigDecimal

fun showMessage(view: View,messager: CharSequence){
    Snackbar.make(view, messager, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}

fun showError(view: View,messager: CharSequence){
    vibrate()
    PlaySound.playError()
    Snackbar.make(view, messager, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}

fun edTextChangesToFlowable(view: TextView): Flowable<String>
        = RxTextView.textChanges(view)
    .map {
        it.toString()
    }
    //.debounce(300, TimeUnit.MILLISECONDS)
    .distinctUntilChanged()
    //.skip(1)
    .toFlowable(BackpressureStrategy.BUFFER)

class KeyKode{
    companion object {
        val KEY_MENU = 12 // 5
        val KEY_DELL = 16 // 9
        val KEY_LOAD = 11 // 4
        val KEY_ADD = 8 // 1
    }
}

public fun vibrate(){
    val vibrator = App.appComponent.getAppContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(500)
    }
}

class EventKeyClick(val keyCode:Int,val id:Int)




