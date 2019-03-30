package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import kotlinx.serialization.Serializable



class CreatePallet : MetaObj(type = Type.CREATE_PALLET) {

    val stringProducts: MutableList<StringProduct> = mutableListOf()

    fun newStringProduct() {
        StringProduct()
    }

    fun dellStringProduct(index: Int) {
        stringProducts.removeAt(index)
    }
}