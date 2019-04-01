package com.anit.fastpallet4.presentaition.ui.screens.inventory


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.anit.fastpallet4.R

import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.inventory.InventoryPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
import com.anit.fastpallet4.presentaition.ui.screens.dialogbox.BoxDialogFr
import com.anit.fastpallet4.presentaition.ui.screens.dialogproduct.ProductDialogFr
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.doc_scr.*
import java.io.Serializable
import java.util.*


class InventoryFrScreen : BaseFragment(), InventoryView {


    class InputParamObj(val guid: String) : Serializable

    var inputParamObj: Serializable? = null

    var REQEST_CODE_PRODUCT_DIALOG = 1
    var REQEST_CODE_BOX_DIALOG = 2

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
    var dialogBox: BoxDialogFr? = null

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


        bagDisposable.add((activity as MainActivity).getFlowableBarcode()
            .subscribe {
                if (!presenter.isShowDialog) {
                    presenter.readBarcode(it)
                }
            })

        tv_info.setOnClickListener {
            presenter.onClickInfo()
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQEST_CODE_PRODUCT_DIALOG) {

            if (resultCode == Activity.RESULT_OK) {
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

        if (requestCode == REQEST_CODE_BOX_DIALOG) {

            if (resultCode == Activity.RESULT_OK) {
                var param = data?.getSerializableExtra(BoxDialogFr.PARAM_KEY)
                        as? BoxDialogFr.InputParamObj

                param.let {
                    presenter.saveBox(
                        barcode = param?.barcode,
                        weight = param?.weight ?: 0f,
                        index = param?.index
                    )
                }
            }

        }

        presenter.isShowDialog = false
    }


    override fun showDialogProduct(
        title: String
        , barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float

    ) {


        if (!presenter.isShowDialog) {
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
            dialogProduct!!.setTargetFragment(this, REQEST_CODE_PRODUCT_DIALOG)
            dialogProduct!!.show(transaction, ProductDialogFr.TAG)

            presenter.isShowDialog = true
        }
    }

    override fun showDialogBox(title: String, barcode: String?, weight: Float, date: Date,index:Int?) {
        if (!presenter.isShowDialog) {
            dialogBox = BoxDialogFr.newInstance(
                BoxDialogFr.InputParamObj(
                    title = title,
                    barcode = barcode,
                    weight = weight,
                    date = date,
                    index = index
                )
            )
            val transaction = fragmentManager!!.beginTransaction()
            dialogBox!!.setTargetFragment(this, REQEST_CODE_BOX_DIALOG)
            dialogBox!!.show(transaction, ProductDialogFr.TAG)

            presenter.isShowDialog = true
        }
    }



}