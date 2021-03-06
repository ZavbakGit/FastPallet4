package com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product


import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.createpallet.product.ProductCreatePalletPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
import com.anit.fastpallet4.presentaition.ui.screens.inventory.CreatePalletProductView
import com.anit.fastpallet4.presentaition.ui.util.KeyKode
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.doc_scr.*
import java.io.Serializable

class ProductCreatePalletFrScreen : BaseFragment(), CreatePalletProductView {

    class InputParamObj(val guid: String,val guidStringProduct:String) : Serializable

    var inputParamObj: Serializable? = null

    companion object {
        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): ProductCreatePalletFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = ProductCreatePalletFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ProductCreatePalletPresenter

    @ProvidePresenter
    fun providePresenter() = ProductCreatePalletPresenter(
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
                    tv_info_doc_left.text = it.left
                    tv_info_doc_right.text = it.right

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

        bagDisposable.add((activity as MainActivity).getFlowableBarcode()
            .subscribe {
                if (!it.isNullOrEmpty()){
                    presenter.readBarcode(it!!)
                }
            })

        bagDisposable.add(
            listFrag.publishSubjectKeyClick
                .subscribe {
                    if (it.keyCode == KeyKode.KEY_DELL) {
                        presenter.onClickDell(it.id)
                    }

                }
        )



        presenter.onStart()
    }

    override fun showDialogConfirmDell(id: Int, title: String) {
        AlertDialog.Builder(activity!!)
            .setTitle(title)
            .setMessage("Удалить")
            .setNegativeButton(android.R.string.cancel, null) // dismisses by default
            .setPositiveButton("Да") { dialog, which ->
                presenter.dellPallet(id)
            }
            .setOnCancelListener({ dialog -> "presenter.onErrorCancel()" })
            .show()
    }

}
