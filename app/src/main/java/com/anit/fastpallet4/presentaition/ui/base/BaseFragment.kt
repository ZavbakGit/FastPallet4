package com.anit.fastpallet4.presentaition.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anit.fastpallet4.presentaition.navigation.BackButtonListener
import com.anit.fastpallet4.presentaition.ui.util.showError
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

    fun showSnackbarViewMess(messager: CharSequence) {
        showMessage(view!!,messager)
    }

    fun showSnackbarViewError(messager: CharSequence) {
        showError(view!!,messager)
    }

    fun getFragmentTransaction() = activity!!.supportFragmentManager.beginTransaction()

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }

    abstract fun getLayout(): Int
}