package com.anit.fastpallet4.presentaition.ui.screens.dialogproduct

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import java.io.Serializable


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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inputParamObj = arguments?.getSerializable(PalletCreatePalletFrScreen.PARAM_KEY)
                as? InputParamObj

        var param = inputParamObj as InputParamObj

        val b = AlertDialog.Builder(activity!!)
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
            ) { dialog, whichButton -> dialog.dismiss() }

        val i = activity!!.layoutInflater

        val v = i.inflate(R.layout.dialog_product_scr, null)

        ed_barcode = v.findViewById(R.id.ed_barcode) as EditText
        ed_start = v.findViewById(R.id.ed_start) as EditText
        ed_end = v.findViewById(R.id.ed_end) as EditText
        ed_coff = v.findViewById(R.id.ed_coff) as EditText



        ed_barcode!!.setText(param.barcode)
        ed_start!!.setText(if (param.weightStartProduct == 0) "" else param.weightStartProduct.toString())
        ed_end!!.setText(if (param.weightEndProduct == 0) "" else param.weightEndProduct.toString())
        ed_coff!!.setText(if (param.weightCoffProduct == 0f) "" else param.weightCoffProduct.toString())

        ed_start!!.setSelection(ed_start!!.getText().length)
        ed_end!!.setSelection(ed_end!!.getText().length)
        ed_coff!!.setSelection(ed_coff!!.getText().length)
        ed_barcode!!.setSelection(ed_barcode!!.getText().length)


        b.setView(v)
        return b.create()


    }


}