package com.anit.fastpallet4.presentaition.ui.screens.inventory


import android.content.Intent
import android.os.Bundle
import com.anit.fastpallet4.R

import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.inventory.InventoryPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.anit.fastpallet4.presentaition.ui.screens.dialogproduct.ProductDialogFr
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.doc_scr.*
import java.io.Serializable


class InventoryFrScreen : BaseFragment(), InventoryView {

    class InputParamObj(val guid: String) : Serializable

    var inputParamObj: Serializable? = null

    companion object {

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): InventoryFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = InventoryFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: InventoryPresenter

    var dialogProduct: ProductDialogFr? = null

    @ProvidePresenter
    fun providePresenter() = InventoryPresenter(
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
                .subscribe {
                    presenter.onClickItem(it)
                }
        )




        tv_info.setOnClickListener {
            presenter.onClickInfo()
        }


    }

    override fun showDialogProduct(
        title: String
        , barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float

    ) {

        if (dialogProduct != null
            && dialogProduct!!.dialog != null
            && dialogProduct!!.dialog.isShowing
            && !dialogProduct!!.isRemoving
        ) {
            //dialog is showing so do something
        } else {
            //dialog is not showing

            dialogProduct = ProductDialogFr.newInstance(
                ProductDialogFr.InputParamObj(
                    title = title,
                    barcode = barcode,
                    weightStartProduct = weightStartProduct,
                    weightEndProduct = weightEndProduct,
                    weightCoffProduct = weightCoffProduct
                )
            )
            val transaction = fragmentManager!!.beginTransaction()
            dialogProduct!!.setTargetFragment(this, 8)
            dialogProduct!!.show(transaction, ProductDialogFr.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var param = data?.getSerializableExtra(ProductDialogFr.PARAM_KEY)
                as? ProductDialogFr.InputParamObj

        param.let {
            presenter.saveProduct(
                barcode = param?.barcode,
                weightStartProduct = param?.weightStartProduct ?: 0,
                weightEndProduct = param?.weightEndProduct ?: 0,
                weightCoffProduct = param?.weightCoffProduct ?: 0f
            )
        }


    }

}
