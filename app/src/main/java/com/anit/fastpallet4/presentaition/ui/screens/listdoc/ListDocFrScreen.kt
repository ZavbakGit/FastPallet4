package com.anit.fastpallet4.presentaition.ui.screens.listdoc

import android.os.Bundle
import com.anit.fastpallet4.R
import com.anit.fastpallet4.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.ListDocPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import java.io.Serializable


class ListDocFrScreen : BaseFragment(), BaseView {

    class InputParamObj() : Serializable

    var inputParamObj: Serializable? = null

    companion object {

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): ListDocFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = ListDocFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }


    @InjectPresenter
    lateinit var presenter: ListDocPresenter

    @ProvidePresenter
    fun providePresenter() = ListDocPresenter(
        router = (activity as RouterProvider).getRouter(),
        inputParamObj = arguments?.getSerializable(PARAM_KEY) as? InputParamObj
    )



    override fun getLayout() = R.layout.fr_list_doc_scr
    override fun onBackPressed() = presenter.onBackPressed()


}