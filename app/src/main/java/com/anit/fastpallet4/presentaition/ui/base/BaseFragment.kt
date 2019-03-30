package com.anit.fastpallet4.presentaition.ui.base

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anit.fastpallet4.navigation.BackButtonListener
import com.anit.fastpallet4.presentaition.ui.util.showMessage
import com.arellomobile.mvp.MvpAppCompatFragment
import io.reactivex.disposables.CompositeDisposable


abstract class BaseFragment : MvpAppCompatFragment(), BackButtonListener {

    protected val bagDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        return view
    }

    fun showSnackbarView(messager: CharSequence) {
        showMessage(view!!,messager)
    }

    fun getFragmentTransaction() = activity!!.supportFragmentManager.beginTransaction()

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }

    abstract fun getLayout(): Int
}