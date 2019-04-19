package com.anit.fastpallet4.presentaition.ui.util

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
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
        val KEY_MENU = 12
        val KEY_DELL = 16
        val KEY_LOAD = 11
    }
}

class EventKeyClick(val keyCode:Int,val id:Int)

data class TotalBoxInfo(val weight:Float, val countBox:Int, val countPallet:Int)

fun getTotalBoxInfoByPallet(stringProduct:StringProduct):TotalBoxInfo{
    var listBox =  stringProduct.pallets
        .flatMap {
            it.boxes
        }
        .map {
            BigDecimal(it.weight.toString())
        }


    var weight =  listBox.fold(BigDecimal(0)){ total: BigDecimal, next: BigDecimal ->total.add(next)}

    return TotalBoxInfo(weight = weight.toFloat(),countBox = listBox.size, countPallet = stringProduct.pallets.size)
}

fun getTotalBoxInfoByPallet(inventoryPallet: InventoryPallet):TotalBoxInfo{
    var listBox =  inventoryPallet.stringProduct.boxes
        .map {
            BigDecimal(it.weight.toString())
        }


    var weight =  listBox.fold(BigDecimal(0)){ total: BigDecimal, next: BigDecimal ->total.add(next)}

    return TotalBoxInfo(weight = weight.toFloat(),countBox = listBox.size, countPallet = 1)
}

fun getTotalBoxInfoByPallet(pallet:Pallet):TotalBoxInfo{
    var listBox =  pallet.boxes
        .map {
            BigDecimal(it.weight.toString())
        }

    var weight =  listBox.fold(BigDecimal(0)){ total: BigDecimal, next: BigDecimal ->total.add(next)}

    return TotalBoxInfo(weight = weight.toFloat(),countBox = listBox.size,countPallet = 1)
}




