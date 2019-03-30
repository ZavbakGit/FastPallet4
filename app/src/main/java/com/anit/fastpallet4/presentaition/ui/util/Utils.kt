package com.anit.fastpallet4.presentaition.ui.util

import android.support.design.widget.Snackbar
import android.view.View

fun showMessage(view: View,messager: CharSequence){
    Snackbar.make(view!!, messager, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}