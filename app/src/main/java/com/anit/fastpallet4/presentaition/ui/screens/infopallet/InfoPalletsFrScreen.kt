package com.anit.fastpallet4.presentaition.ui.screens.infopallet


import android.os.Bundle
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.infopallet.InfoPalletPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
import com.anit.fastpallet4.presentaition.ui.util.KeyKode
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.doc_scr.*
import java.io.Serializable


class InfoPalletsFrScreen : BaseFragment(), BaseView {


    class InputParamObj() : Serializable

    var inputParamObj: Serializable? = null


    companion object {

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): InfoPalletsFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = InfoPalletsFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: InfoPalletPresenter


    @ProvidePresenter
    fun providePresenter() = InfoPalletPresenter(
        router = (activity as RouterProvider).getRouter(),
        inputParamObj = arguments?.getSerializable(PARAM_KEY) as? InputParamObj
    )


    override fun getLayout() = R.layout.fr_infopallet_scr
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
//                    tv_info.text = it.info
//                    tv_info_doc_right.text = it.right
//                    tv_info_doc_left.text = it.left
                }
        )


        var transaction = getFragmentTransaction()
        transaction.replace(R.id.conteiner_frame_list, listFrag)
        transaction.commit()


        //Штрихкод
        bagDisposable.add((activity as MainActivity).getFlowableBarcode()
            .subscribe {
                if (!presenter.isShowDialog) {
                    presenter.readBarcode(it?:"")
                }
            })



        bagDisposable.add(
            listFrag.publishSubjectKeyClick
                .subscribe {

                    if (it.keyCode == KeyKode.KEY_LOAD) {
                        presenter.loadInfoPallet()
                    }

                }
        )

        presenter.onStart()


    }


}
