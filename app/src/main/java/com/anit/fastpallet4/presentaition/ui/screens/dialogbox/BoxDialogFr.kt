package com.anit.fastpallet4.presentaition.ui.screens.dialogbox

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.anit.fastpallet4.R
import com.anit.fastpallet4.common.formatDate

import io.reactivex.disposables.CompositeDisposable

import java.io.Serializable
import java.util.*


class BoxDialogFr : DialogFragment() {
    class InputParamObj(
        val title: String? = null,
        val index: Int? = null,
        var barcode: String? = null,
        var countBox: Int = 1,
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
    var ed_count_box: EditText? = null
    var ed_date: EditText? = null


    protected val bagDisposable = CompositeDisposable()

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inputParamObj = arguments?.getSerializable(PARAM_KEY)
                as? InputParamObj

        var param = inputParamObj as InputParamObj

        val builder = AlertDialog.Builder(activity!!)
            .setTitle(param.title)
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, whichButton ->

                var weight = ed_weight?.text.toString().toFloatOrNull() ?: 0f
                var countBox = ed_count_box?.text.toString().toIntOrNull() ?: 1


                var param = InputParamObj(
                    index = (inputParamObj as InputParamObj).index,
                    barcode = ed_barcode?.text.toString(),
                    weight = weight,
                    countBox = countBox
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
        ed_count_box = v.findViewById(R.id.ed_count_box) as EditText
        ed_date = v.findViewById(R.id.ed_date) as EditText

        ed_weight!!.setSelection(ed_weight!!.text.length)




        ed_barcode!!.setText(param.barcode)
        ed_weight!!.setText(if (param.weight == 0f) "" else param.weight.toString())
        ed_count_box!!.setText(if (param.countBox == 0) "1" else param.countBox.toString())
        ed_date!!.setText(formatDate(param.date))

        ed_weight!!.setOnFocusChangeListener { view, b ->
            (view as EditText).setSelection(0,view.text.length)
        }

        ed_count_box!!.setOnFocusChangeListener { view, b ->
            (view as EditText).setSelection(0,view.text.length)
        }

        builder.setView(v)
        return builder.create()


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }
}