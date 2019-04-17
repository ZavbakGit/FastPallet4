package com.anit.fastpallet4.presentaition.ui.screens.inventory

import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.util.*

interface CreatePalletView : BaseView {
    @StateStrategyType(SkipStrategy::class)
    fun showDialogProduct(
        title: String
        , barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    )

    @StateStrategyType(SkipStrategy::class)
    fun showDialogBox(
        title: String
        , barcode: String?
        , weight: Float
        , date: Date
        , index: Int?
    )

}