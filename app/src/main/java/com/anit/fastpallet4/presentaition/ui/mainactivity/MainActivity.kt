package com.anit.fastpallet4.presentaition.ui.mainactivity


import android.os.Bundle
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.MainPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseActivity
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.util.showError
import com.anit.fastpallet4.presentaition.ui.util.showMessage
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gladkikh.mylibrary.BarcodeHelper
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity :BaseActivity(), BaseView, RouterProvider {


    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var barcodeHelper: BarcodeHelper
    fun getFlowableBarcode() = barcodeHelper.getDataFlowable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeHelper = BarcodeHelper(this)
    }

    @ProvidePresenter
    fun provideMainPresenter() = MainPresenter(getRouter())

    override fun showSnackbarViewMess(messager: CharSequence) {
        showMessage(ac_main_root,messager)
    }

    override fun showSnackbarViewError(messager: CharSequence) {
        showError(ac_main_root,messager)
    }

    override fun pressBackTo() = presenter.onBackPressed()
    override fun getIdConteiner() = R.id.conteiner_frame
    override fun getNavigator() = SupportAppNavigator(this, getIdConteiner())
    override fun getLayout() = R.layout.activity_main
}
