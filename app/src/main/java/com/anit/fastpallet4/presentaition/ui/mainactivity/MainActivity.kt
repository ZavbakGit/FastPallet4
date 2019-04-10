package com.anit.fastpallet4.presentaition.ui.mainactivity


import android.os.Bundle
import com.anit.fastpallet4.R
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.MainPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseActivity
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.util.showError
import com.anit.fastpallet4.presentaition.ui.util.showMessage
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gladkikh.mylibrary.BarcodeHelper
import com.gladkikh.preference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class MainActivity :BaseActivity(), BaseView, RouterProvider {


    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var barcodeHelper: BarcodeHelper



    fun getFlowableBarcode() = barcodeHelper.getDataFlowable()

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        var typeTsd = BarcodeHelper.TYPE_TSD.getTypeTSD(preferenceHelper.getTypeTsd()?.toIntOrNull())
            ?:BarcodeHelper.TYPE_TSD.ATOL_SMART_DROID

        barcodeHelper = BarcodeHelper(this,typeTsd)
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
