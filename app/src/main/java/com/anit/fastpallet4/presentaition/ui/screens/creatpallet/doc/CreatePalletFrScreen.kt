package com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc


import android.os.Bundle
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.createpallet.doc.CreatePalletPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.doc_scr.*
import java.io.Serializable

class CreatePalletFrScreen : BaseFragment(), BaseView {

    class InputParamObj(val guid: String) : Serializable

    var inputParamObj: Serializable? = null

    companion object {

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): CreatePalletFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = CreatePalletFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: CreatePalletPresenter

    @ProvidePresenter
    fun providePresenter() = CreatePalletPresenter(
        router = (activity as RouterProvider).getRouter(),
        inputParamObj = arguments?.getSerializable(PARAM_KEY) as? InputParamObj
    )


    override fun getLayout() = R.layout.doc_scr
    override fun onBackPressed() = presenter.onBackPressed()


    override fun onStart() {
        super.onStart()

        var listFrag = MyListFragment.newInstance(presenter.getViewModelFlowable()
            .map {
                it.list
            })

        bagDisposable.add(
            presenter.getViewModelFlowable()
                .subscribe {
                    tv_info.text = it.info
                }
        )


        var transaction = getFragmentTransaction()
        transaction.replace(R.id.conteiner_frame_list, listFrag)
        transaction.commit()

        bagDisposable.add(
            listFrag.publishSubjectItemClick
                .subscribe{
                    presenter.onClickItem(it)
                }
        )

        tv_info.setOnClickListener {
            presenter.readBarcode("10.1")
        }


    }

}
