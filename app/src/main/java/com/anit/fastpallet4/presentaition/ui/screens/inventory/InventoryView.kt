package com.anit.fastpallet4.presentaition.ui.screens.inventory

import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface InventoryView:BaseView{
    @StateStrategyType(SkipStrategy::class)
    fun showDialogProduct(title:String
                          ,barcode: String?
                          ,weightStartProduct: Int
                          ,weightEndProduct: Int
                          ,weightCoffProduct: Float)

}