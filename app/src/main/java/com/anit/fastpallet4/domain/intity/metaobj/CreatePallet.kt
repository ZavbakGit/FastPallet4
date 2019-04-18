package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import kotlinx.serialization.Serializable
import java.util.*


class CreatePallet : MetaObj(type = Type.CREATE_PALLET) {

    var stringProducts: MutableList<StringProduct> = mutableListOf()

    fun addStringProduct(stringProduct: StringProduct) {
        stringProducts.add(stringProduct)
    }

    fun dellStringProduct(index: Int) {
        stringProducts.removeAt(index)
    }

    override fun save() {
        stringProducts.forEach {
            if (it.guid.isNullOrEmpty()) {
                it.guid = UUID.randomUUID().toString()
            }
        }

        stringProducts
            .flatMap { it.pallets }
            .forEach {
                if (it.guid.isNullOrEmpty()) {
                    it.guid = UUID.randomUUID().toString()
                }
            }

        stringProducts
            .flatMap { it.pallets }
            .flatMap { it.boxes }
            .forEach {
                if (it.guid.isNullOrEmpty()) {
                    it.guid = UUID.randomUUID().toString()
                }
            }

        super.save()
    }
}