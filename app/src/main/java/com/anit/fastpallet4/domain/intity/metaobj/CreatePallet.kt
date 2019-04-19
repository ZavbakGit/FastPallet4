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

    fun dellStringProductByGuid(guid: String) {
        stringProducts.removeAll { it.guid.equals(guid,true) }
    }

    fun getStringProductByGuid(guid: String):StringProduct? {
       return stringProducts.find { it.guid.equals(guid,true) }
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