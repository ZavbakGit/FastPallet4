package com.anit.fastpallet4.presentaition.ui.screens.dialogbox

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.TextView
import com.anit.fastpallet4.R
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.util.edTextChangesToFlowable

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.rxkotlin.combineLatest
import kotlinx.android.synthetic.main.dialog_product_scr.*
import java.io.Serializable
import java.util.*


class BoxDialogFr : DialogFragment() {
    class InputParamObj(
        val title: String? = null,
        val index: Int? = null,
        var barcode: String? = null,
        var weight: Float = 0f,
        var date: Date = Date()
    ) : Serializable

    var inputParamObj: Serializable? = null

    companion object {
        val TAG = "ProductDialogFr"

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): BoxDialogFr {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = BoxDialogFr()
            fragment.arguments = bundle
            return fragment
        }
    }

    var ed_barcode: EditText? = null
    var ed_weight: EditText? = null
    var ed_date: EditText? = null


    protected val bagDisposable = CompositeDisposable()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inputParamObj = arguments?.getSerializable(PARAM_KEY)
                as? InputParamObj

        var param = inputParamObj as InputParamObj

        val b = AlertDialog.Builder(activity!!)
            .setTitle(param.title)
            .setPositiveButton(
                "OK"
            ) { dialog, whichButton ->

                var weight = ed_weight?.text.toString().toFloatOrNull() ?: 0f


                var param = InputParamObj(
                    index = (inputParamObj as InputParamObj).index,
                    barcode = ed_barcode?.text.toString(),
                    weight = weight
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

        val v = i.inflate(R.layout.dialog_box_scr, null)

        ed_barcode = v.findViewById(R.id.ed_barcode) as EditText
        ed_weight = v.findViewById(R.id.ed_weight) as EditText
        ed_date = v.findViewById(R.id.ed_date) as EditText




        ed_barcode!!.setText(param.barcode)
        ed_weight!!.setText(if (param.weight == 0f) "" else param.weight.toString())
        ed_date!!.setText(formatDate(param.date))


        b.setView(v)
        return b.create()


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }
}