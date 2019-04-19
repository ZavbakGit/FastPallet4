package com.anit.fastpallet4.domain.intity.metaobj

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.util.*


class StringProduct {

    var guid: String? = null
    var number: String? = null
    var barcode: String? = null

    var guidProduct: String? = null
    var nameProduct: String? = null
    var codeProduct: String? = null
    var ed: String? = null

    var weightBarcode: String? = null
    var weightStartProduct: Int = 0
    var weightEndProduct: Int = 0
    var weightCoffProduct: Float = 0f

    var edCoff: Float = 1f
    var count: Float = 0f
    var countBox: Int = 0



    var dataChanged: Date? = null
    var isWasLoadedLastTime: Boolean? = null


    val boxes: MutableList<Box> = mutableListOf()
    var pallets: MutableList<Pallet> = mutableListOf()

    fun addPallet(pallet: Pallet) {
        pallets.add(pallet)
    }

    fun addBox(box: Box) {
        boxes.add(box)
    }

    fun dellPalletByGuid(guid: String) {
        pallets.removeAll { it.guid.equals(guid,true) }
    }

    fun dellBoxByGuid(guid: String) {
        boxes.removeAll { it.guid.equals(guid,true) }
    }

    fun getPalletByGuid(guid:String):Pallet?{
        return  pallets.find { it.guid.equals(guid,true)}
    }


    fun getBoxByGuid(guid:String):Box?{
        return  boxes.find { it.guid.equals(guid,true)}
    }

}