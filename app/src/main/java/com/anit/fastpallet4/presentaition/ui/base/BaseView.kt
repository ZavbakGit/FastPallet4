package com.anit.fastpallet4.presentaition.ui.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView:MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun showSnackbarViewMess(messager: CharSequence)

    @StateStrategyType(SkipStrategy::class)
    fun showSnackbarViewError(messager: CharSequence)


}