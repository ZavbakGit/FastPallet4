package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.domain.intity.metaobj.*
import com.anit.fastpallet4.domain.intity.metaobj.Status.*
import com.anit.fastpallet4.domain.usecase.UseCaseAddTestData
import java.util.*

class InteractorAddTestData : UseCaseAddTestData {


    fun getPallet(number: String): Pallet {
        val pallet = Pallet()
        pallet.number = number
        (0..100).forEach {
            val box = Box()
            box.countBox = 2
            box.weight = 25.55f
            box.data = Date()
            pallet.addBox(box)
        }

        return pallet
    }

    fun getStringProduct(number: String): StringProduct {
        val stringProduct = StringProduct()
        stringProduct.dataChanged = Date()
        stringProduct.isWasLoadedLastTime = false
        stringProduct.nameProduct = "Продукт №$number"

        (1..10).forEach {
            stringProduct.addPallet(getPallet("$it"))
        }

        return stringProduct

    }

    override fun add() {
        (1..2).map {
            val doc = CreatePallet()
            doc.description = "Фрмирование паллет №$it"
            doc.number = "$it"
            doc.status = LOADED

            (0..10).map {
                doc.addStringProduct(getStringProduct("$it"))
            }

            doc.save()
        }


    }

}