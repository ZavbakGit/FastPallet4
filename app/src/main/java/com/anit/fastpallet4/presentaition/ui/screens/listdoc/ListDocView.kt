package com.anit.fastpallet4.presentaition.ui.screens.listdoc

import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ListDocView:BaseView {
    @StateStrategyType(SkipStrategy::class)
    fun showMainMenu(listmenu:List<Pair<Int,String>>)

    @StateStrategyType(SkipStrategy::class)
    fun showDialogConfirmDell(id:Int,title:String)



}