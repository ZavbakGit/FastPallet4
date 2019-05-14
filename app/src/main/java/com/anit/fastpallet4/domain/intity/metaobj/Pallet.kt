package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.extra.SummPalletInfo
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*


class Pallet {
    var number: String? = null
    var barcode: String? = null
    var guid: String? = null

    var dataChanged: Date? = null
    var count: Float = 0f
    var countBox: Int = 0

    var guidProduct: String? = null
    var nameProduct: String? = null
    var state: String? = null
    var sclad: String? = null


    val boxes: MutableList<Box> = mutableListOf()

    fun addBox(box: Box) {
        boxes.add(box)
    }

    fun dellBoxByGuid(guid: String) {
        boxes.removeAll { it.guid.equals(guid, true) }
    }

    fun getBoxByGuid(guid: String): Box? {
        return boxes.find { it.guid.equals(guid, true) }
    }


    //Это итог с списка коробок
    fun getSummPalletInfoFromBoxes(): SummPalletInfo {
        var summ =  boxes.fold(SummPalletInfo()) { total: SummPalletInfo, box: Box ->


            var couBox = box.countBox
            var cou  = box.weight

            total.countBox = total.countBox + couBox
            total.count = BigDecimal(total.count.toString()).add(BigDecimal(cou.toString())).toFloat()
            return@fold total
        }

        summ.countPallet = 1
        return  summ
    }

    //Это итог с сервера
    fun getSummPalletInfoFromServer(): SummPalletInfo{
        return SummPalletInfo(countBox = countBox,count = count,countPallet = 1)
    }
}