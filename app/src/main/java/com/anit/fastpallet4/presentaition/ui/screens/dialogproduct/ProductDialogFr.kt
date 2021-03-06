package com.anit.fastpallet4.presentaition.ui.screens.dialogproduct

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.TextView
import com.anit.fastpallet4.R
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.util.edTextChangesToFlowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.combineLatest
import java.io.Serializable
import java.util.concurrent.TimeUnit


class ProductDialogFr : DialogFragment() {
    class InputParamObj(
        val title: String?,
        var barcode: String? = null,
        var weightStartProduct: Int = 0,
        var weightEndProduct: Int = 0,
        var weightCoffProduct: Float = 0f
    ) : Serializable

    var inputParamObj: Serializable? = null

    companion object {
        val TAG = "ProductDialogFr"

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): ProductDialogFr {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = ProductDialogFr()
            fragment.arguments = bundle
            return fragment
        }
    }

    var ed_barcode: EditText? = null
    var ed_start: EditText? = null
    var ed_end: EditText? = null
    var ed_coff: EditText? = null
    var ed_weight: EditText? = null


    protected val bagDisposable = CompositeDisposable()

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inputParamObj = arguments?.getSerializable(PalletCreatePalletFrScreen.PARAM_KEY)
                as? InputParamObj

        var param = inputParamObj as InputParamObj

        val builder = AlertDialog.Builder(activity!!)
            .setTitle(param.title)
            .setPositiveButton(
                "OK"
            ) { dialog, whichButton ->

                var param = InputParamObj(
                    title = null,
                    barcode = ed_barcode?.text.toString(),
                    weightStartProduct = ed_start?.text.toString().toIntOrNull() ?: 0,
                    weightEndProduct = ed_end?.text.toString().toIntOrNull() ?: 0,
                    weightCoffProduct = ed_coff?.text.toString().toFloatOrNull() ?: 0f

                )

                val intent = Intent()
                intent.putExtra(PARAM_KEY, param)
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

                dialog.dismiss()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, whichButton ->

                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)

                dialog.dismiss()
            }

        val i = activity!!.layoutInflater

        val v = i.inflate(R.layout.dialog_product_scr, null)

        ed_barcode = v.findViewById(R.id.ed_barcode) as EditText
        ed_start = v.findViewById(R.id.ed_start) as EditText
        ed_end = v.findViewById(R.id.ed_end) as EditText
        ed_coff = v.findViewById(R.id.ed_coff) as EditText
        ed_weight = v.findViewById(R.id.ed_weight) as EditText



        ed_barcode!!.setText(param.barcode)
        ed_start!!.setText(if (param.weightStartProduct == 0) "" else param.weightStartProduct.toString())
        ed_end!!.setText(if (param.weightEndProduct == 0) "" else param.weightEndProduct.toString())
        ed_coff!!.setText(if (param.weightCoffProduct == 0f) "" else param.weightCoffProduct.toString())

        ed_start!!.setSelection(ed_start!!.getText().length)
        ed_end!!.setSelection(ed_end!!.getText().length)
        ed_coff!!.setSelection(ed_coff!!.getText().length)
        ed_barcode!!.setSelection(ed_barcode!!.getText().length)


        ed_start!!.setOnFocusChangeListener { view, b ->
            (view as EditText).setSelection(0, view.text.length)
        }

        ed_end!!.setOnFocusChangeListener { view, b ->
            (view as EditText).setSelection(0, view.text.length)
        }

        ed_coff!!.setOnFocusChangeListener { view, b ->
            (view as EditText).setSelection(0, view.text.length)
        }


        builder.setView(v)
        return builder.create()


    }

    override fun onStart() {
        super.onStart()
        bagDisposable.add((activity as MainActivity).getFlowableBarcode()
            .subscribe {
                ed_barcode!!.setText(it)
            })


        var flwBarcode = edTextChangesToFlowable(ed_barcode as TextView)
        var flwStart = edTextChangesToFlowable(ed_start as TextView)
        var flwEnd = edTextChangesToFlowable(ed_end as TextView)
        var flwCoff = edTextChangesToFlowable(ed_coff as TextView)


        bagDisposable.add(
            flwBarcode
                .map {
                    it
                }
                //.delay(1000, TimeUnit.MILLISECONDS)
                .combineLatest(flwStart)
                .map {
                    it
                }
                .map {
                    object {
                        var barcode = it.first
                        var start = it.second.toIntOrNull() ?: 0
                        var end = 0
                        var coff = 0f
                    }
                }
                .combineLatest(flwEnd)
                .map {
                    it.first.end = it.second.toIntOrNull() ?: 0
                    return@map it.first
                }
                .combineLatest(flwCoff)
                .map {
                    it.first.coff = it.second.toFloatOrNull() ?: 0f
                    return@map it.first
                }
                .map {
                    getWeightByBarcode(
                        barcode = it.barcode,
                        start = it.start,
                        finish = it.end,
                        coff = it.coff
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var str = it.toString()
                    ed_weight!!.setText(str)

                    var text: SpannableStringBuilder



                    //Разукрасим штрихкод
                    var strBar = ed_barcode!!.text.toString()
                    try {
                        text = SpannableStringBuilder(strBar)
                        val style = ForegroundColorSpan(Color.rgb(255, 0, 0))
                        text.setSpan(
                            style,
                            (ed_start?.text.toString().toIntOrNull() ?: 0) - 1,
                            (ed_end?.text.toString().toIntOrNull() ?: 0),
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        ed_barcode!!.text = text
                    } catch (e: Exception) {
                        ed_barcode!!.setText(strBar)
                    }


                }
        )


    }

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }
}