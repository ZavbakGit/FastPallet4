package com.anit.fastpallet4.presentaition.ui.screens.inventory

import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.util.*

interface CreatePalletProductView : BaseView {

    @StateStrategyType(SkipStrategy::class)
    fun showDialogConfirmDell(id:Int,title:String)
}