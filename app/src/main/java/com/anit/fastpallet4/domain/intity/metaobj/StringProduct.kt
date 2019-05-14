package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.extra.SummPalletInfo
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.math.BigDecimal
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
    var countPallet: Int = 0 //Это под вопросом


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
        pallets.removeAll { it.guid.equals(guid, true) }
    }

    fun dellBoxByGuid(guid: String) {
        boxes.removeAll { it.guid.equals(guid, true) }
    }

    fun getPalletByGuid(guid: String): Pallet? {
        return pallets.find { it.guid.equals(guid, true) }
    }


    fun getBoxByGuid(guid: String): Box? {
        return boxes.find { it.guid.equals(guid, true) }
    }

    //Это итог с сервера
    fun getSummPalletInfoFromServer(): SummPalletInfo {
        return SummPalletInfo(countBox = countBox, count = count, countPallet = countPallet)
    }

    fun getSummPalletInfoFromPalletBoxes(): SummPalletInfo {
        return pallets.fold(SummPalletInfo()) { total: SummPalletInfo, pallet: Pallet ->
            var sum = pallet.getSummPalletInfoFromBoxes()

            var pal = sum.countPallet
            var couBox = sum.countBox
            var cou = sum.count

            total.countPallet = total.countPallet + pal
            total.countBox = total.countBox + couBox
            total.count = BigDecimal(total.count.toString()).add(BigDecimal(cou.toString())).toFloat()
            return@fold total
        }
    }

    fun getSummPalletInfoForAction(): SummPalletInfo {
        var summBox = boxes.fold(SummPalletInfo()) { total: SummPalletInfo, box: Box ->

            var pal = 0
            var couBox = box.countBox
            var cou = box.weight

            total.countPallet = pal
            total.countBox = total.countBox + couBox
            total.count = BigDecimal(total.count.toString()).add(BigDecimal(cou.toString())).toFloat()
            return@fold total
        }

        var summPall = pallets.fold(SummPalletInfo()) { total: SummPalletInfo, pallet: Pallet ->

            var pal = 1
            var couBox = pallet.countBox
            var cou = pallet.count


            total.countPallet = total.countPallet + pal
            total.countBox = total.countBox + couBox
            total.count = BigDecimal(total.count.toString()).add(BigDecimal(cou.toString())).toFloat()
            return@fold total
        }




        return SummPalletInfo(
            countPallet = summBox.countPallet + summPall.countPallet
            , countBox = summBox.countBox + summPall.countBox
            , count = BigDecimal(summBox.count.toString()).add(BigDecimal(summPall.count.toString())).toFloat()
        )

    }

    fun getSummPalletInfoForInventory(): SummPalletInfo {
        return boxes.fold(SummPalletInfo()) { total: SummPalletInfo, box: Box ->

            var pal = 1
            var couBox = box.countBox
            var cou = box.weight

            total.countPallet = pal
            total.countBox = total.countBox + couBox
            total.count = BigDecimal(total.count.toString()).add(BigDecimal(cou.toString())).toFloat()

            return@fold total

        }
    }


}